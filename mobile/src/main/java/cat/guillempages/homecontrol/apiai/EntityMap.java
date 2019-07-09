package cat.guillempages.homecontrol.apiai;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cat.guillempages.homecontrol.apiai.action.AbstractAction;
import cat.guillempages.homecontrol.hass.entities.lights.AllLights;
import cat.guillempages.homecontrol.hass.entities.lights.ColoredLeds;
import cat.guillempages.homecontrol.hass.entities.lights.SideLights;
import cat.guillempages.homecontrol.hass.entities.lights.WarmLeds;
import cat.guillempages.homecontrol.hass.entities.mediaplayers.KitchenPlayer;
import cat.guillempages.homecontrol.hass.entities.mediaplayers.LivingRoomPlayer;
import cat.guillempages.homecontrol.hass.entities.mediaplayers.OfficePlayer;
import cat.guillempages.homecontrol.hass.entities.remotes.HarmonyHub;

/**
 * Helper class to map API.AI entities to HASS entities.
 *
 * Created by guillem on 29/05/2017.
 */
public class EntityMap {
    private static final Map<String, String> ENTITIES_MAP = new HashMap<>();

    static {
        // Media Players
        ENTITIES_MAP.put("büro", OfficePlayer.ENTITY_ID);
        ENTITIES_MAP.put("küche", KitchenPlayer.ENTITY_ID);
        ENTITIES_MAP.put("wohnzimmer", LivingRoomPlayer.ENTITY_ID);

        // Remotes
        ENTITIES_MAP.put("harmony", HarmonyHub.ENTITY_ID);

        // Lights
        ENTITIES_MAP.put("alle", AllLights.ENTITY_ID);
        ENTITIES_MAP.put("seiten", SideLights.ENTITY_ID);
        ENTITIES_MAP.put("kalte", ColoredLeds.ENTITY_ID);
        ENTITIES_MAP.put("fernseher", WarmLeds.ENTITY_ID);
    }

    /**
     * Get the {@link AbstractAction entity} according to the given string.
     *
     * @param entity The desired entity's name.
     * @return The desired entity, or the default entity if not found.
     */
    @NonNull
    public static String getEntityName(@NonNull final String entity) {
        final String result = ENTITIES_MAP.get(entity.toLowerCase(Locale.getDefault()));
        if (result == null) {
            return "";
        } else {
            return result;
        }
    }
}
