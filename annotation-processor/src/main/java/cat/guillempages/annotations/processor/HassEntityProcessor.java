package cat.guillempages.annotations.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import cat.guillempages.homecontrol.annotations.HassEntity;
import cat.guillempages.homecontrol.annotations.HassEntity.EntityType;

@SupportedAnnotationTypes("cat.guillempages.homecontrol.annotations.HassEntity")
public class HassEntityProcessor extends AbstractProcessor {

    static final String CLASS_NAME = "HassEntityMap";
    static final String PACKAGE_NAME = "cat.guillempages.homecontrol.hass";

    private static final String ENTITIES_PACKAGE_NAME = PACKAGE_NAME + ".entities";
    private static final ClassName HASS_CLASS = ClassName.get(PACKAGE_NAME, "Hass");

    private static final ClassName LOG_CLASS =
        ClassName.get("android.util", "Log");
    private static final ClassName NULLABLE_ANNOTATION =
        ClassName.get("android.support.annotation", "Nullable");

    private final Builder mEntityMapClass;

    /**
     * Constructor.
     */
    public HassEntityProcessor() {

        mEntityMapClass = TypeSpec.classBuilder(CLASS_NAME)
            .addModifiers(Modifier.PUBLIC);
        final FieldSpec tag = FieldSpec.builder(String.class, "TAG", Modifier.PRIVATE, Modifier
            .STATIC, Modifier.FINAL).initializer(CLASS_NAME + ".class.getSimpleName()").build();
        mEntityMapClass.addField(tag);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set,
                           final RoundEnvironment roundEnvironment) {

        final EnumMap<EntityType, List<TypeElement>> hassEntities = readEntities(
            roundEnvironment.getElementsAnnotatedWith(HassEntity.class));

        final MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
            .addParameter(HASS_CLASS, "hass", Modifier.FINAL)
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc("Constructor.\n\n")
            .addJavadoc(
                "@param hass The {@link Hass} instance that the entities need to use to "
                    + "communicate with the\n"
                    + "            Hass server.\n");

        for (final EntityType type : EntityType.values()) {
            createEntityCollection(type, hassEntities.get(type), constructor);
        }

        mEntityMapClass.addMethod(constructor.build());

        try {
            JavaFile.builder(PACKAGE_NAME, mEntityMapClass.build())
                .indent("    ")
                .build()
                .writeTo(processingEnv.getFiler());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Create the field and getter method for a given entity type. If the given collection does
     * not contain any entity for that type, the field will not be created, and the created
     * getter method will just return "null". Also, the entities passed in the collection will be
     * added to the generated map in the constructor.
     *
     * @param type The entity type to put the given entities to.
     * @param entityList The list of entities of the given type to put into the map.
     * @param constructor The constructor {@link MethodSpec.Builder} instance, to be able to fill
     *                   the entity collection in the generated constructor.
     */
    private void createEntityCollection(final EntityType type,
                                        final List<TypeElement> entityList,
                                        final MethodSpec.Builder constructor) {
        final ClassName entityCategory = ClassName.get(ENTITIES_PACKAGE_NAME, type.name());
        if (entityList.isEmpty()) {
            createEmptyGetter(entityCategory);
        } else {
            // If there are some entities in the category, create a field to store them
            final FieldSpec field = FieldSpec.builder(
                ParameterizedTypeName.get(
                    ClassName.get(Map.class), ClassName.get(String.class), entityCategory),
                "m" + type.name() + "List")
                .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
                .initializer("new $T()", HashMap.class)
                .build();
            mEntityMapClass.addField(field);

            createEntityGetter(entityCategory, field);

            constructor.addComment(type.name() + " entities");
            // Create the needed instances in the constructor.
            for (final TypeElement entityType : entityList) {
                constructor.addStatement("$N.put($T.ENTITY_ID, new $T($L))", field,
                    ClassName.get(entityType),
                    ClassName.get(entityType),
                    "hass");
            }
            constructor.addCode("\n");
        }
    }

    /**
     * Create a {@link MethodSpec.Builder} with the default parameters and javadoc for the getter
     * method.
     *
     * @param entityClass The class to create the getter for.
     * @return The builder with the method definition. Can be used to add the code and create the
     * method.
     */
    private MethodSpec.Builder createGetterMethodBuilder(final ClassName entityClass) {
        final String type = entityClass.simpleName();
        return MethodSpec.methodBuilder("get" + type)
            .returns(entityClass)
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(NULLABLE_ANNOTATION)
            .addJavadoc("Get the instance of the " + type + " for the given entity id.\n\n")
            .addJavadoc("@param entityId the entity to get the " + type + " for.\n")
            .addJavadoc("@return The {@link " + type + "} entity or null if the entity was not "
                + "found.\n")
            .addParameter(String.class, "entityId", Modifier.FINAL);
    }

    /**
     * Create a getter for an Entity category without any entity. When used, the getter will
     * print a warning and return null.
     *
     * @param entityClass The class to create the getter for.
     */
    private void createEmptyGetter(final ClassName entityClass) {
        final String type = entityClass.simpleName();
        final MethodSpec.Builder getEntity = createGetterMethodBuilder(entityClass);
        getEntity.addStatement("$T.w(TAG, $S)", LOG_CLASS, "No " + type + " entities available.");
        getEntity.addStatement("return null");
        mEntityMapClass.addMethod(getEntity.build());
    }


    /**
     * Create a specific getter for an entity category.
     *
     * @param entityClass The class to create the getter for.
     * @param field       The field that the getter will read from.
     */
    private void createEntityGetter(final ClassName entityClass, final FieldSpec field) {
        final String type = entityClass.simpleName();
        final MethodSpec.Builder getEntity = createGetterMethodBuilder(entityClass);
        getEntity.addStatement("final " + type + " result = " + field.name + ".get(" +
            "entityId" + ")");
        getEntity.addStatement("$T.d(TAG, \"Got " + type + " entity for \" + entityId  + "
            + "\": \" + " + "result)", LOG_CLASS);
        getEntity.addStatement("return result");
        mEntityMapClass.addMethod(getEntity.build());
    }

    /**
     * Get a map with all entities annotated with the {@link HassEntity} annotation. This method
     * will map the entities according to the {@link HassEntity#value()}.
     *
     * @param hassEntities The collection of Elements with the HassEntity annotation.
     * @return
     */
    private EnumMap<EntityType, List<TypeElement>> readEntities(
        final Collection<? extends Element> hassEntities) {
        final EnumMap<EntityType, List<TypeElement>> entityCollections =
            new EnumMap<>(EntityType.class);
        for (final EntityType type : EntityType.values()) {
            entityCollections.put(type, new ArrayList<TypeElement>());
        }


        for (final Element element : hassEntities) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING,
                    "Annotation " + HassEntity.class.getSimpleName()
                        + " can only be applied to class.");
                processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING,
                    "    " + element + " has wrong type: " + element.getKind());
            }
            final TypeElement typeElement = (TypeElement) element;
            if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
                // Abstract classes do not need to be added to the entity list.
                continue;
            }
            final HassEntity annotation = typeElement.getAnnotation(HassEntity.class);
            entityCollections.get(annotation.value()).add(typeElement);
        }
        return entityCollections;
    }

}
