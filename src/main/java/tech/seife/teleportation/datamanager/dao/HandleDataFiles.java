package tech.seife.teleportation.datamanager.dao;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import tech.seife.teleportation.Teleportation;
import tech.seife.teleportation.homes.Home;
import tech.seife.teleportation.signs.Sign;
import tech.seife.teleportation.signs.SignManager;
import tech.seife.teleportation.warps.Warp;
import tech.seife.teleportation.warps.WarpManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HandleDataFiles implements HandleData {

    private final Teleportation plugin;
    private final Gson gson;

    public HandleDataFiles(Teleportation plugin) {
        this.plugin = plugin;
        gson = plugin.getFileManager().getGson();
    }


    @Override
    public boolean isHomeValidUuid(UUID owner, String homeName) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getHomeFile()), JsonObject.class);

        if (jsonObject == null || jsonObject.entrySet() == null) return false;

        for (Map.Entry<String, JsonElement> ids : jsonObject.entrySet()) {
            for (Map.Entry<String, JsonElement> details : ids.getValue().getAsJsonObject().entrySet()) {
                if (details.getKey().equals("homes") && details.getValue().getAsJsonObject().get(homeName) != null) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public Home getHomeUuid(UUID owner, String homeName) {
        if (isHomeValidUuid(owner, homeName)) {
            JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getHomeFile()), JsonObject.class);

            for (Map.Entry<String, JsonElement> ids : jsonObject.entrySet()) {
                if (ids.getKey().equals(owner.toString())) {
                    for (Map.Entry<String, JsonElement> details : ids.getValue().getAsJsonObject().entrySet()) {
                        if (details.getKey().equals("homes")) {
                            return new Home(details.getValue().getAsJsonObject().get("id").getAsInt(), UUID.fromString(ids.getKey()), ids.getValue().getAsJsonObject().get("ownerName").getAsString(), homeName, getLocationFromJsonObj(details.getValue().getAsJsonObject().get(homeName).getAsJsonObject().getAsJsonObject()));
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void saveHome(Home home) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getHomeFile()), JsonObject.class);

        if (isHomeValidUuid(home.getOwnerUuid(), home.getHomeName())) return;

        JsonObject owner = null;
        JsonObject homes = null;

        if (jsonObject.get(home.getOwnerUuid().toString()) != null) {
            owner = jsonObject.getAsJsonObject(home.getOwnerUuid().toString());

            homes = new JsonObject();

            for (Map.Entry<String, JsonElement> entry : owner.entrySet()) {
                homes.add(entry.getKey(), entry.getValue());
            }

        } else {
            owner = new JsonObject();
            homes = new JsonObject();
        }

        homes.add(home.getHomeName(), locationToJsonObject(home.getLocation()));
        homes.addProperty("id", home.getId());

        owner.remove("homes");
        owner.addProperty("ownerName", home.getOwnerName());
        owner.add("homes", homes);

        jsonObject.remove(home.getOwnerUuid().toString());
        jsonObject.add(home.getOwnerUuid().toString(), owner);

        System.out.println(jsonObject.toString());

        plugin.getFileManager().saveHomeFile(gson.fromJson(jsonObject, Map.class));
    }

    @Override
    public void removeHome(Home home) {
        if (isHomeValidUuid(home.getOwnerUuid(), home.getHomeName())) {
            JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getHomeFile()), JsonObject.class);

            for (Map.Entry<String, JsonElement> ids : jsonObject.entrySet()) {
                if (ids.getKey().equals(home.getOwnerUuid().toString())) {
                    if (ids.getValue().getAsJsonObject().get("homes").getAsJsonObject().get("id").getAsInt() == home.getId()) {
                        ids.getValue().getAsJsonObject().get("homes").getAsJsonObject().remove(home.getHomeName());

                        jsonObject.remove(ids.getKey());
                        jsonObject.add(ids.getKey(), ids.getValue());

                        plugin.getFileManager().saveHomeFile(gson.fromJson(jsonObject, Map.class));
                    }
                }
            }
        }
    }

    @Override
    public int getLatestIdOfHomes() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getHomeFile()), JsonObject.class);
        return Math.max(jsonObject.size(), 1);
    }

    @Override
    public List<String> getHomeNamesOfPlayer(UUID playerUuid) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getHomeFile()), JsonObject.class);

        List<String> homeNames = new ArrayList<>();
        for (Map.Entry<String, JsonElement> ids : jsonObject.entrySet()) {
            for (Map.Entry<String, JsonElement> details : ids.getValue().getAsJsonObject().entrySet()) {
                if (details.getValue().getAsJsonObject().get("ownerUuid").equals(playerUuid)) {
                    homeNames.add(details.getValue().getAsJsonObject().get("homeName").getAsString());
                }
            }
        }
        return homeNames;
    }

    @Override
    public int getNumberOfHomes(UUID playerUuid) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getHomeFile()), JsonObject.class);

        for (Map.Entry<String, JsonElement> ids : jsonObject.entrySet()) {
            if (ids.getValue().getAsJsonObject().get(playerUuid.toString()) != null) {
                if (ids.getValue().getAsJsonObject().get(playerUuid.toString()).getAsJsonObject().get("homes") != null) {
                    return ids.getValue().getAsJsonObject().get(playerUuid.toString()).getAsJsonObject().get("homes").getAsJsonObject().entrySet().size();
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isHomeValidUsername(String owner, String homeName) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getHomeFile()), JsonObject.class);
        for (Map.Entry<String, JsonElement> ids : jsonObject.entrySet()) {
            if (ids.getValue().getAsJsonObject().get("ownerName").getAsString().equals(owner)) {
                for (Map.Entry<String, JsonElement> details : ids.getValue().getAsJsonObject().entrySet()) {
                    if (details.getValue().getAsString().equals(homeName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public boolean isValidInvitation(UUID invitedUuid, UUID invitedBy, String homeName) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getInvitationsFile()), JsonObject.class);

        for (Map.Entry<String, JsonElement> inviters : jsonObject.entrySet()) {
            if (inviters.getValue().getAsJsonObject().get(invitedUuid.toString()) != null) {
                List<String> homes = getHomeNamesOfPlayer(invitedUuid);
                if (homes.contains(homeName)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void saveInvitation(String invited, String inviter, String homeName) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getInvitationsFile()), JsonObject.class);

        JsonArray homeList = new JsonArray();
        if (jsonObject.get(inviter) != null) {
            if (jsonObject.get(inviter).getAsJsonObject().get(invited) != null) {
                homeList.addAll(jsonObject.get(inviter).getAsJsonObject().get(invited).getAsJsonObject().get("homeIds").getAsJsonArray());
                jsonObject.remove(invited);
            }
            homeList.add(homeName);
            jsonObject.add(invited, homeList);

            plugin.getFileManager().saveInvitationsFile(gson.fromJson(jsonObject, Map.class));
        }
    }

    @Override
    public void removeInvitation(UUID invited, UUID inviter, String homeName) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getInvitationsFile()), JsonObject.class);

        JsonArray homeList = new JsonArray();
        if (jsonObject.get(inviter.toString()) != null) {
            if (jsonObject.get(inviter.toString()).getAsJsonObject().get(invited.toString()) != null) {
                homeList.addAll(jsonObject.get(inviter.toString()).getAsJsonObject().get(invited.toString()).getAsJsonObject().get("homeIds").getAsJsonArray());

                for (JsonElement element : homeList) {
                    if (element.getAsString().equals(homeName)) {
                        homeList.remove(element);
                        jsonObject.remove(invited.toString());
                    }
                }
            }
            jsonObject.add(invited.toString(), homeList);
            plugin.getFileManager().saveInvitationsFile(gson.fromJson(jsonObject, Map.class));
        }

    }

    @Override
    public void saveWarp(Warp warp) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getWarpFile()), JsonObject.class);

        if (jsonObject.get(String.valueOf(warp.getId())) != null) {
            JsonObject id = new JsonObject();

            id.addProperty("name", warp.getName());
            id.add("location", locationToJsonObject(warp.getLocation()));

            plugin.getFileManager().saveWarpFile(gson.fromJson(jsonObject, Map.class));
        }
    }

    @Override
    public void loadWarps() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getWarpFile()), JsonObject.class);

        WarpManager warpManager = plugin.getWarpManager();

        jsonObject.entrySet().forEach(entry -> warpManager.addWarp(entry.getValue().getAsJsonObject().get("name").getAsString(), getLocationFromJsonObj(entry.getValue().getAsJsonObject().get("location").getAsJsonObject())));
    }

    @Override
    public void removeWarp(Warp warp) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getWarpFile()), JsonObject.class);

        if (jsonObject.get(String.valueOf(warp.getId())) != null) {
            jsonObject.remove(String.valueOf(warp.getId()));
            plugin.getFileManager().saveWarpFile(gson.fromJson(jsonObject, Map.class));
        }

    }

    @Override
    public void saveSpawnLocation(Location location) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getSpawnFile()), JsonObject.class);

        jsonObject.add("spawn", locationToJsonObject(location));
        plugin.getFileManager().saveSpawnFile(gson.fromJson(jsonObject, Map.class));
    }

    @Override
    public void loadSpawnLocation() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getSpawnFile()), JsonObject.class);

        if (jsonObject.get("spawn") != null) {
            plugin.getDataHolder().setSpawnLocation(getLocationFromJsonObj(jsonObject.get("spawn").getAsJsonObject()));
        }
    }

    @Override
    public void loadSigns() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getSignFile()), JsonObject.class);

        SignManager signManager = plugin.getSignManager();

        jsonObject.entrySet().forEach(entry -> {
            signManager.addSign(Integer.parseInt(entry.getKey()), getLocationFromJsonObj(entry.getValue().getAsJsonObject().getAsJsonObject("signLocation")), getLocationFromJsonObj(entry.getValue().getAsJsonObject().getAsJsonObject("locationToTeleport")));
        });

    }

    @Override
    public void saveSign(Sign sign) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getSignFile()), JsonObject.class);

        if (jsonObject.get(String.valueOf(sign.getId())) != null) {
            JsonObject details = new JsonObject();

            details.add("signLocation", locationToJsonObject(sign.getSignLocation()));
            details.add("locationToTeleport", locationToJsonObject(sign.getLocationToTeleport()));

            plugin.getFileManager().saveSignFile(gson.fromJson(jsonObject, Map.class));

        }
    }

    @Override
    public void removeSign(Sign sign) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getSignFile()), JsonObject.class);

        if (jsonObject.get(String.valueOf(sign.getId())) != null) {
            jsonObject.remove(String.valueOf(sign.getId()));
            plugin.getFileManager().saveSignFile(gson.fromJson(jsonObject, Map.class));
        }
    }

    @Override
    public int getLatestIdOfSigns() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getSignFile()), JsonObject.class);

        return jsonObject.size() >= 1 ? jsonObject.size() : 1;
    }

    @Override
    public void savePortals(String name, Location portalLocation, Location linkedLocation) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getPortalsFile()), JsonObject.class);

        if (jsonObject.get(name) == null) {
            JsonObject portal = new JsonObject();

            portal.add("portalLocation", locationToJsonObject(portalLocation));
            portal.add("linkedLocation", locationToJsonObject(linkedLocation));

            jsonObject.add(name, portal);

            plugin.getFileManager().savePortalsFile(gson.fromJson(jsonObject, Map.class));

        }
    }

    @Override
    public void removePortal(String portalName) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getPortalsFile()), JsonObject.class);

        if (jsonObject.get(portalName) != null) {
            jsonObject.remove(portalName);
            plugin.getFileManager().savePortalsFile(gson.fromJson(jsonObject, Map.class));
        }
    }

    @Override
    public void loadPortals() {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getPortalsFile()), JsonObject.class);

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            plugin.getDataHolder().getPortalLocationWarp().put(getLocationFromJsonObj(entry.getValue().getAsJsonObject().getAsJsonObject("portalLocation")), getLocationFromJsonObj(entry.getValue().getAsJsonObject().getAsJsonObject("linkedLocation")));
        }
    }

    @Override
    public boolean doesPortalExist(String portalName) {
        JsonObject jsonObject = gson.fromJson(gson.toJson(plugin.getFileManager().getPortalsFile()), JsonObject.class);

        return jsonObject.get(portalName) != null;
    }

    private JsonObject locationToJsonObject(Location location) {
        JsonObject loc = new JsonObject();

        loc.addProperty("world", location.getWorld().getName());
        loc.addProperty("x", location.getBlockX());
        loc.addProperty("y", location.getBlockY());
        loc.addProperty("z", location.getBlockZ());

        return loc;
    }

    private Location getLocationFromJsonObj(JsonObject object) {
        if (Bukkit.getWorld(object.get("world").getAsString()) != null) {
            return new Location(Bukkit.getWorld(object.get("world").getAsString()), object.get("x").getAsInt(), object.get("y").getAsInt(), object.get("z").getAsInt());
        }
        return null;
    }

}
