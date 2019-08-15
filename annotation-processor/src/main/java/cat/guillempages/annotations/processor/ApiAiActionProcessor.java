package cat.guillempages.annotations.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

import cat.guillempages.homecontrol.annotations.ApiAiAction;

/**
 * Annotation processor for the ApiAiAction annotation. This annotation will automatically create
 * a map class with all Api.AI actions that are annotated.
 */
@SupportedAnnotationTypes("cat.guillempages.homecontrol.annotations.ApiAiAction")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ApiAiActionProcessor extends AbstractProcessor {
    private static final String CLASS_NAME = "ActionMap";
    private static final String PACKAGE_NAME = "cat.guillempages.homecontrol.apiai";

    private static final ClassName CONTEXT_CLASS = ClassName.get("android.content", "Context");
    private static final ClassName NONNULL_ANNOTATION =
        ClassName.get("androidx.annotation", "NonNull");

    private static final ClassName RESULT_CLASS = ClassName.get("ai.api.model", "Result");

    private static final ClassName ABSTRACT_ACTION_CLASS =
        ClassName.get("cat.guillempages.homecontrol.apiai.action", "AbstractAction");
    private static final ClassName EMPTY_ACTION_CLASS =
        ClassName.get("cat.guillempages.homecontrol.apiai.action", "EmptyAction");

    private final TypeSpec.Builder mEntityMapClass;

    /**
     * Constructor.
     */
    public ApiAiActionProcessor() {

        mEntityMapClass = TypeSpec.classBuilder(CLASS_NAME)
            .addModifiers(Modifier.PUBLIC)
            .addJavadoc("Map ApiAiActions to the intent strings form the ApiAi model.\n");

        final FieldSpec tag = FieldSpec.builder(String.class, "TAG")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
            .initializer(CLASS_NAME + ".class.getSimpleName()")
            .build();
        mEntityMapClass.addField(tag);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> set,
                           final RoundEnvironment roundEnvironment) {

        final FieldSpec actionsMap = FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(
            Map.class), ClassName.get(String.class), ABSTRACT_ACTION_CLASS), "mActionMap")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .initializer("new $T<>()", ClassName.get(HashMap.class))
            .build();
        mEntityMapClass.addField(actionsMap);

        final FieldSpec defaultAction = FieldSpec.builder(ABSTRACT_ACTION_CLASS, "mDefaultAction")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .build();

        mEntityMapClass.addField(defaultAction);

        addConstructor(roundEnvironment.getElementsAnnotatedWith(ApiAiAction.class),
            actionsMap, defaultAction);
        addGetActionMethod(actionsMap, defaultAction);
        addExecuteMethod();

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
     * Create the constructor and add it to the class.
     *
     * @param apiActions    The list of actions that the constructor needs to initialize.
     * @param actionsMap    The field containing the actions map.
     * @param defaultAction The default action field (so that it can also be initialized).
     */
    private void addConstructor(final Collection<? extends Element> apiActions,
                                final FieldSpec actionsMap,
                                final FieldSpec defaultAction) {
        final Builder constructor = MethodSpec.constructorBuilder()
            .addParameter(CONTEXT_CLASS, "context", Modifier.FINAL)
            .addParameter(
                ClassName.get(HassEntityProcessor.PACKAGE_NAME, HassEntityProcessor.CLASS_NAME),
                "hassEntities", Modifier.FINAL)
            .addModifiers(Modifier.PUBLIC)

            .addJavadoc("Constructor.\n\n")
            .addJavadoc("@param context The context.\n")
            .addJavadoc("@param hassEntities the Hass entity list.\n")

            .addStatement(defaultAction.name + " = new $T(context)", EMPTY_ACTION_CLASS)
            .addCode("\n");
        addActionsToConstructor(constructor, actionsMap, apiActions);
        mEntityMapClass.addMethod(constructor.build());
    }

    /**
     * Add the "execute" method to the class, so that actions can be executed.
     */
    private void addExecuteMethod() {
        final MethodSpec execute = MethodSpec.methodBuilder("execute")
            .addAnnotation(NONNULL_ANNOTATION)
            .addModifiers(Modifier.PUBLIC)
            .returns(String.class)
            .addParameter(ParameterSpec.builder(RESULT_CLASS, "result", Modifier.FINAL)
                .addAnnotation(NONNULL_ANNOTATION)
                .build())

            .addJavadoc("Execute the action corresponding to the result.\n\n")
            .addJavadoc("@param result The API.ai result.\n")
            .addJavadoc("@return The String corresponding to the action execution.\n")

            .addStatement("return getAction(result.getAction()).execute(result)")

            .build();
        mEntityMapClass.addMethod(execute);
    }

    /**
     * Add the "getAction" method to the class, so that actions can be gotten from the map.
     *
     * @param actionsMap    The field containing the actions map.
     * @param defaultAction The default action to return if not found.
     */
    private void addGetActionMethod(final FieldSpec actionsMap, final FieldSpec defaultAction) {
        final MethodSpec getAction = MethodSpec.methodBuilder("getAction")
            .returns(ABSTRACT_ACTION_CLASS)
            .addModifiers(Modifier.PRIVATE)
            .addAnnotation(NONNULL_ANNOTATION)
            .addParameter(ParameterSpec.builder(String.class, "action", Modifier.FINAL)
                .addAnnotation(NONNULL_ANNOTATION).build())

            .addJavadoc("Get the {@link AbstractAction action} according to the given string.\n\n")
            .addJavadoc("@param action The desired action's name.\n")
            .addJavadoc("@return The desired action, or the default action if not found.\n")

            .addStatement("final $T result = $N.get(action.toLowerCase(Locale.getDefault()));",
                ABSTRACT_ACTION_CLASS, actionsMap.name)
            .beginControlFlow("if (result == null)")
            .addStatement("return " + defaultAction.name)
            .nextControlFlow("else")
            .addStatement("return result")
            .endControlFlow()

            .build();
        mEntityMapClass.addMethod(getAction);
    }

    /**
     * Add the "addAction" method. This is an auxiliary method for the constructor to be able to
     * add the actions to the map.
     *
     * @param actionsMap The field containing the actions map.
     */
    private MethodSpec addAddActionMethod(final FieldSpec actionsMap) {
        final MethodSpec addAction = MethodSpec.methodBuilder("addAction")
            .addModifiers(Modifier.PRIVATE)
            .addParameter(
                ParameterSpec.builder(ABSTRACT_ACTION_CLASS, "action", Modifier.FINAL)
                    .addAnnotation(NONNULL_ANNOTATION).build())

            .addJavadoc("Add the given action to the map.\n\n")
            .addJavadoc("@param action The action to add to the map.\n")

            .addStatement(
                actionsMap.name + ".put(action.getName().toLowerCase($T.getDefault()), action)",
                Locale.class)

            .build();
        mEntityMapClass.addMethod(addAction);
        return addAction;
    }

    /**
     * Add the annotated actions to the map in the constructor code.
     *
     * @param constructor Reference to the constructor method.
     * @param actionsMap  The field to add the actions to.
     * @param apiActions  The annotated actions to add to the constructor.
     */
    private void addActionsToConstructor(final Builder constructor,
                                         final FieldSpec actionsMap,
                                         final Collection<? extends Element> apiActions) {
        final MethodSpec addAction = addAddActionMethod(actionsMap);
        for (final Element element : apiActions) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING,
                    "Annotation " + ApiAiAction.class.getSimpleName()
                        + " can only be applied to class.");
                processingEnv.getMessager().printMessage(Kind.MANDATORY_WARNING,
                    "    " + element + " has wrong type: " + element.getKind());
            }
            final TypeElement typeElement = (TypeElement) element;
            if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
                // Abstract classes do not need to be added to the entity list.
                continue;
            }
            final ApiAiAction annotation = typeElement.getAnnotation(ApiAiAction.class);

            final String haasEntities = annotation.isHaas() ? ", hassEntities" : "";

            constructor.addStatement(addAction.name + "(new $T(context" + haasEntities + "))",
                typeElement);
        }
    }
}
