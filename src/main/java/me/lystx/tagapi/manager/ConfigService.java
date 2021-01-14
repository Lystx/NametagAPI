package me.lystx.tagapi.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import me.lystx.tagapi.utils.Document;
import me.lystx.tagapi.utils.NametagGroup;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Getter
public class ConfigService {

    private final File directory;
    private final File config;
    private final Document document;
    private final List<NametagGroup> nametagGroups;

    private boolean noDamageWhenSameNametag;
    private boolean updateOnJoin;
    private boolean updateOnQuit;
    private boolean updateOnWorldChange;
    private boolean updateAllPlayers;

    public ConfigService() {
        this.nametagGroups = new LinkedList<>();
        this.directory = new File("plugins/NametagAPI");
        this.directory.mkdirs();
        this.config = new File(this.directory, "config.json");
        this.document = Document.fromFile(this.config);

        this.load();
    }

    public NametagGroup getGroup(String name) {
        for (NametagGroup nametagGroup : this.nametagGroups) {
            if (nametagGroup.getName().equalsIgnoreCase(name)) {
                return nametagGroup;
            }
        }
        return null;
    }

    public void load() {
        if (!this.config.exists()) {
            JsonArray jsonArray = new JsonArray();

            jsonArray.add(new Document().appendAll(new NametagGroup(
                    "default",
                    "§7Default §8| §7",
                    "§7",
                    9999,
                    null
            )).getJsonObject());
            jsonArray.add(new Document().appendAll(new NametagGroup(
                    "Admin",
                    "§4Admin §8| §7",
                    "§7",
                    0,
                    "nametag.admin"
            )).getJsonObject());

            this.document.append("groups", jsonArray);
            this.document.append("settings", new Document()
                .append("noDamageWhenSameNametag", true)
                .append("updateOnJoin", true)
                .append("updateOnQuit", true)
                .append("updateOnWorldChange", true)
                .append("updateAllPlayers", true)
            );
            this.document.save();
        }
        this.nametagGroups.clear();
        for (JsonElement groups : this.document.getJsonArray("groups")) {
            Document group = new Document((JsonObject) groups);
            this.nametagGroups.add(group.getObject(group.getJsonObject(), NametagGroup.class));
        }
        Document settings = this.document.getDocument("settings");
        this.noDamageWhenSameNametag = settings.getBoolean("noDamageWhenSameNametag");
        this.updateOnJoin = settings.getBoolean("updateOnJoin");
        this.updateOnQuit = settings.getBoolean("updateOnQuit");
        this.updateOnWorldChange = settings.getBoolean("updateOnWorldChange");
        this.updateAllPlayers = settings.getBoolean("updateAllPlayers");
    }

    public void save() {

        JsonArray jsonArray = new JsonArray();
        for (NametagGroup nametagGroup : this.nametagGroups) {
            jsonArray.add(new Document(nametagGroup).getJsonObject());
        }

        this.document.save(this.config);
    }
}
