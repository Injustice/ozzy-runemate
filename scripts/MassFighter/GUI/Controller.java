package scripts.MassFighter.GUI;

import com.runemate.game.api.client.ClientUI;
import com.runemate.game.api.hybrid.Environment;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.Player;
import com.runemate.game.api.hybrid.local.hud.interfaces.Health;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.queries.NpcQueryBuilder;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.hybrid.region.Players;
import com.runemate.game.api.hybrid.util.io.ManagedProperties;
import com.runemate.game.api.script.framework.AbstractScript;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scripts.MassFighter.Data.SkillPotion;
import scripts.MassFighter.MassFighter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class Controller {

    @FXML
    private ListView<String> availableMonsters;
    @FXML
    private Button addLoot;
    @FXML
    private TextField lootName;
    @FXML
    private ListView<String> selectedMonsters;
    @FXML
    private Slider eatValue;
    @FXML
    private TextField tileRange;
    @FXML
    private ListView<String> foodSelection;
    @FXML
    private CheckBox soulsplit;
    @FXML
    private CheckBox lootInCombat;
    @FXML
    private CheckBox buryBones;
    @FXML
    private Slider targetSlider;
    @FXML
    private Button npcButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TextField criticalHitpoints;
    @FXML
    private CheckBox quickPray;
    @FXML
    private Slider prayValue;
    @FXML
    private Slider tagSlider;
    @FXML
    private Button btnStart;
    @FXML
    private TextField lootValue;
    @FXML
    private Button btnAddToAlch;
    @FXML
    private ListView<String> availableBoosts;
    @FXML
    private ListView<String> selectedBoosts;
    @FXML
    private Button boostButton;
    @FXML
    private CheckBox attackCombatMonsters;
    @FXML
    private CheckBox bypassReachable;
    @FXML
    private Button btnAddToNotepaper;
    @FXML
    private Slider boostRefreshPercentage;
    @FXML
    private CheckBox reequipAmmunition;
    @FXML
    private CheckBox soulsplitPerm;
    @FXML
    private Slider soulsplitPercentage;
    @FXML
    private TextField txtFoodInput;
    @FXML
    private Button btnAddFood;
    @FXML
    private Button btnRemoveFood;
    @FXML
    private ComboBox<String> abilitiesMode;
    @FXML
    private VBox guiVbox;
    @FXML
    private ListView<String> pickupLoot;
    @FXML
    private ListView<String> alchemyLoot;
    @FXML
    private ListView<String> magicNotepaperLoot;
    @FXML
    private Button btnRemovePickupLoot;
    @FXML
    private Button btnRemoveAlchemyLoot;
    @FXML
    private Button btnRemoveNotepaperLoot;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnLoad;
    @FXML
    private CheckBox logoutFood;
    @FXML
    private CheckBox logoutHitpoints;
    @FXML
    private CheckBox teleportFood;
    @FXML
    private CheckBox teleportHitpoints;



    private List<String> getAvailableMonsters(Area area, String action) {
        List<String> availableNpcs = new ArrayList<>();
        NpcQueryBuilder getNearbyNpcs = Npcs.newQuery().within(area).actions(action);
        Collection<Npc> npcs = getNearbyNpcs.results();
        if (!npcs.isEmpty()) {
            npcs.stream().filter(n -> n != null && !availableNpcs.contains(n.getName())).forEach(n -> availableNpcs.add(n.getName()));
        }
        return availableNpcs;
    }

    public void initialize() {


        guiVbox.setOnMouseMoved(event -> {
            if (!selectedMonsters.getItems().isEmpty() && isNumeric(tileRange.getText())) {
                btnStart.setText("Start Bot");
            } else {
                btnStart.setText("We're not setup to fight yet.");
            }
        });


        selectedBoosts.getItems().clear();

        abilitiesMode.getItems().addAll("Legacy/OSRS", "Full Ability Usage", "Revolution Mode");
        abilitiesMode.getSelectionModel().select(0);

        for (SkillPotion skillPotion : SkillPotion.values()) {
            availableBoosts.getItems().add(skillPotion.toString());
        }

        refreshButton.setOnAction(event -> {
            availableMonsters.getItems().remove(0, availableMonsters.getItems().size());
            final Player player = Players.getLocal();
            if (player != null) {
                availableMonsters.getItems().addAll(getAvailableMonsters(new Area.Circular(player.getPosition(), 20), "Attack"));
            }
        });
        npcButton.setOnAction(event -> {
            if (!selectedMonsters.getSelectionModel().getSelectedItems().isEmpty()) {
                selectedMonsters.getItems().removeAll(selectedMonsters.getSelectionModel().getSelectedItems());
            } else if (!availableMonsters.getSelectionModel().getSelectedItems().isEmpty()) {
                availableMonsters.getSelectionModel().getSelectedItems().stream().filter(s -> !selectedMonsters.getItems().contains(s)).forEach(s -> selectedMonsters.getItems().add(s));
            }
        });
        boostButton.setOnAction(event -> {
            if (!selectedBoosts.getSelectionModel().getSelectedItems().isEmpty()) {
                selectedBoosts.getItems().removeAll(selectedBoosts.getSelectionModel().getSelectedItems());
            } else if (!availableBoosts.getSelectionModel().getSelectedItems().isEmpty()) {
                availableBoosts.getSelectionModel().getSelectedItems().stream().filter(s -> !selectedBoosts.getItems().contains(s)).forEach(s -> selectedBoosts.getItems().add(s));
            }
        });
        btnAddFood.setOnAction(event -> {
            if (!txtFoodInput.getText().isEmpty() && !foodSelection.getItems().contains(txtFoodInput.getText())) {
                foodSelection.getItems().add(txtFoodInput.getText());
                txtFoodInput.clear();
            }
        });
        btnRemoveFood.setOnAction(event -> {
            if (!foodSelection.getSelectionModel().isEmpty()) {
                foodSelection.getItems().removeAll(foodSelection.getSelectionModel().getSelectedItems());
            }
        });

        btnSave.setOnAction(event -> {
            save();
        });
        btnLoad.setOnAction(event -> {
            load();
        });

        soulsplitPerm.setOnAction(event -> soulsplitPercentage.setDisable(soulsplitPerm.isSelected()));

        addLoot.setOnAction(event -> {
            addToList(lootName.getText(), pickupLoot.getItems());
        });

        btnAddToAlch.setOnAction(event -> {
            String lootInput = lootName.getText();
            addToList(lootInput, pickupLoot.getItems());
            addToList(lootInput, alchemyLoot.getItems());
        });

        btnAddToNotepaper.setOnAction(event -> {
            String lootInput = lootName.getText();
            addToList(lootInput, pickupLoot.getItems());
            addToList(lootInput, magicNotepaperLoot.getItems());
        });

        btnRemovePickupLoot.setOnAction(event -> {
            removeFromList(pickupLoot.getSelectionModel().getSelectedItems(), pickupLoot.getItems());
        });

        btnRemoveAlchemyLoot.setOnAction(event -> {
            removeFromList(alchemyLoot.getSelectionModel().getSelectedItems(), alchemyLoot.getItems());
        });

        btnRemoveNotepaperLoot.setOnAction(event -> {
            removeFromList(magicNotepaperLoot.getSelectionModel().getSelectedItems(), magicNotepaperLoot.getItems());
        });


        soulsplit.setOnAction(this::togglePrayer);
        quickPray.setOnAction(this::togglePrayer);
        btnStart.setOnAction(this::start);

        // Init fields
        tileRange.setText("10");
        eatValue.setValue(Health.getMaximum() / 2);
        criticalHitpoints.setText(Double.toString((Health.getMaximum() / 5)));
        prayValue.setValue(50);
        if (Environment.isRS3()) {
            criticalHitpoints.setText("500");
        } else {
            criticalHitpoints.setText("5");
        }
        boostRefreshPercentage.setValue(50);

    }

    private void addToList(String input, ObservableList<String> target) {
        if (input != null && !input.isEmpty() && !target.contains(input)) {
            target.add(input);
        }
    }

    private void removeFromList(ObservableList<String> input, ObservableList<String> target) {
        if (input != null && !input.isEmpty()) {
            input.forEach(target::remove);
        }
    }


    private void togglePrayer(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(soulsplit)) {
            quickPray.setSelected(false);
        } else {
            soulsplit.setSelected(false);
        }
    }


    // Start button pressed, start the script
    private void start(ActionEvent actionEvent) {
        if (run()) {
            MassFighter.setupRunning = false;
            closeUI();
        } else {
            System.out.println("Failed to start, incorrect settings?");
        }
    }

    private boolean run() {
        if (!selectedMonsters.getItems().isEmpty() && isNumeric(tileRange.getText()) && isNumeric(criticalHitpoints.getText())) {
            // Create a settings object and store the settings
            if (!foodSelection.getItems().isEmpty()) {
                List<String> foodNames = foodSelection.getItems().stream().map(String::toLowerCase).collect(Collectors.toList());
                Settings.foodNames = foodNames.toArray(new String[foodNames.size()]);
                Settings.eatValue = (int) eatValue.getValue();
            }
            if (quickPray.isSelected() || soulsplit.isSelected()) {
                Settings.prayValue = (int) prayValue.getValue();
            }
            if (tagSlider.getValue() > 1) {
                Settings.tagMode = true;
                Settings.tagSelection = (int) tagSlider.getValue();
            }
            Settings.attackCombatMonsters = attackCombatMonsters.isSelected();
            Settings.bypassReachable = bypassReachable.isSelected();
            Settings.targetSelection = (int) targetSlider.getValue();
            Settings.lootInCombat = lootInCombat.isSelected();
            Settings.useAbilities = abilitiesMode.getSelectionModel().getSelectedIndex() != 0;
            Settings.fightRadius = Integer.valueOf(tileRange.getText());
            Settings.fightArea = new Area.Circular(Players.getLocal().getPosition(), Settings.fightRadius);
            Settings.revolutionMode = abilitiesMode.getSelectionModel().getSelectedIndex() == 2;
            Settings.soulsplitPermanent = soulsplitPerm.isSelected();
            Settings.soulsplitPercentage = (int) soulsplitPercentage.getValue();
            Settings.quickPray = quickPray.isSelected();
            Settings.useSoulsplit = soulsplit.isSelected();
            Settings.criticalHitpoints = Integer.valueOf(criticalHitpoints.getText());
            Settings.buryBones = buryBones.isSelected();
            Settings.equipAmmunition = reequipAmmunition.isSelected();
            Settings.foodLogout = logoutFood.isSelected();
            Settings.foodTeleport = teleportFood.isSelected();
            Settings.healthLogout = logoutHitpoints.isSelected();
            Settings.healthTeleport = teleportHitpoints.isSelected();
            if (!selectedBoosts.getItems().isEmpty()) {
                Settings.selectedPotions = selectedBoosts.getItems().toArray(new String[(selectedBoosts.getItems().size())]);
                Settings.boostRefreshPercentage = boostRefreshPercentage.getValue();
            }
            if (!pickupLoot.getItems().isEmpty()) {
                List<String> lootNames = new ArrayList<>();
                lootNames.addAll(pickupLoot.getItems().stream().map(String::toLowerCase).collect(Collectors.toList()));
                Settings.lootNames = lootNames.toArray(new String[lootNames.size()]);
            }
            if (isNumeric(lootValue.getText())) {
                Settings.lootByValue = true;
                Settings.lootValue = Double.valueOf(lootValue.getText());
            }
            if (!alchemyLoot.getItems().isEmpty()) {
                List<String> alchLoot = new ArrayList<>();
                alchLoot.addAll(alchemyLoot.getItems().stream().map(String::toLowerCase).collect(Collectors.toList()));
                Settings.alchLoot = alchLoot.toArray(new String[alchLoot.size()]);
            }
            if (!magicNotepaperLoot.getItems().isEmpty()) {
                List<String> notepaperLoot = new ArrayList<>();
                notepaperLoot.addAll(magicNotepaperLoot.getItems().stream().map(String::toLowerCase).collect(Collectors.toList()));
                Settings.notepaperLoot = notepaperLoot.toArray(new String[notepaperLoot.size()]);
            }
            Settings.npcNames = selectedMonsters.getItems().toArray(new String[(selectedMonsters.getItems().size())]);
            return true;
        }
        return false;
    }

    private boolean load() {
        AbstractScript script = Environment.getScript();
        if (script != null) {
            ManagedProperties managedProperties = script.getSettings();
            if (managedProperties != null) {
                String npcProperty = managedProperties.getProperty("npcNames");
                if (npcProperty != null && npcProperty.length() > 0) {
                    String[] npcNames = managedProperties.getProperty("npcNames").split(",");
                    selectedMonsters.getItems().clear();
                    selectedMonsters.getItems().addAll(npcNames);
                    //
                    String[] lootNames = managedProperties.getProperty("lootNames").split(",");
                    pickupLoot.getItems().clear();
                    pickupLoot.getItems().addAll(lootNames);
                    //
                    String[] alchLootNames = managedProperties.getProperty("alchLoot").split(",");
                    alchemyLoot.getItems().clear();
                    alchemyLoot.getItems().addAll(alchLootNames);
                    //
                    String[] notepaperLootNames = managedProperties.getProperty("notepaperLoot").split(",");
                    magicNotepaperLoot.getItems().clear();
                    magicNotepaperLoot.getItems().addAll(notepaperLootNames);

                    String targetSelectionString = managedProperties.getProperty("targetSelection");
                    if (isNumeric(targetSelectionString)) {
                        targetSlider.setValue(Integer.valueOf(targetSelectionString));
                    }
                    logoutFood.setSelected(Boolean.valueOf(managedProperties.getProperty("foodLogout")));
                    teleportFood.setSelected(Boolean.valueOf(managedProperties.getProperty("foodTeleport")));
                    logoutHitpoints.setSelected(Boolean.valueOf(managedProperties.getProperty("hitpointsLogout")));
                    teleportHitpoints.setSelected(Boolean.valueOf(managedProperties.getProperty("hitpointsTeleport")));
                    lootInCombat.setSelected(Boolean.valueOf(managedProperties.getProperty("lootInCombat")));
                    abilitiesMode.getSelectionModel().select(Integer.valueOf(managedProperties.getProperty("abilityMode")));
                    soulsplit.setSelected(Boolean.valueOf(managedProperties.getProperty("useSoulsplit")));
                    buryBones.setSelected(Boolean.valueOf(managedProperties.getProperty("buryBones")));
                    quickPray.setSelected(Boolean.valueOf(managedProperties.getProperty("quickPray")));
                    attackCombatMonsters.setSelected(Boolean.valueOf(managedProperties.getProperty("attackCombatMonsters")));
                    bypassReachable.setSelected(Boolean.valueOf(managedProperties.getProperty("bypassReachable")));
                    reequipAmmunition.setSelected(Boolean.valueOf(managedProperties.getProperty("equipAmmunition")));
                    soulsplitPerm.setSelected(Boolean.valueOf(managedProperties.getProperty("soulsplitPermanent")));
                    //
                    String soulsplitPercentageString = managedProperties.getProperty("soulsplitPercentage");
                    if (isNumeric(soulsplitPercentageString)) {
                        soulsplitPercentage.setValue(Integer.valueOf(soulsplitPercentageString));
                    }
                    //
                    String lootValueString = managedProperties.getProperty("lootValue");
                    if (isNumeric(lootValueString)) {
                        lootValue.setText(lootValueString);
                    }
                    //
                    String tagSelectionString = managedProperties.getProperty("tagSelection");
                    if (isNumeric(tagSelectionString)) {
                        tagSlider.setValue(Integer.valueOf(tagSelectionString));
                    }
                    //
                    String[] foodNames =  managedProperties.getProperty("foodNames").split(",");
                    foodSelection.getItems().clear();
                    foodSelection.getItems().addAll(foodNames);
                    //
                    String fightRadiusString = managedProperties.getProperty("fightRadius");
                    if (isNumeric(fightRadiusString)) {
                        tileRange.setText(fightRadiusString);
                    }
                    //
                    String eatValueString = managedProperties.getProperty("eatValue");
                    if (isNumeric(eatValueString)) {
                        eatValue.setValue(Double.valueOf(eatValueString));
                    }
                    //
                    String prayValueString = managedProperties.getProperty("prayValue");
                    if (isNumeric(prayValueString)) {
                        prayValue.setValue(Double.valueOf(prayValueString));
                    }
                    //
                    String criticalHitpointsString = managedProperties.getProperty("criticalHitpoints");
                    if (isNumeric(criticalHitpointsString)) {
                        criticalHitpoints.setText(criticalHitpointsString);
                    }
                    //
                    String[] potionNames = managedProperties.getProperty("selectedPotions").split(",");
                    selectedBoosts.getItems().clear();
                    selectedBoosts.getItems().addAll(potionNames);
                    //
                    String boostRefreshPercentageString = managedProperties.getProperty("boostRefreshPercentage");
                    if (isNumeric(boostRefreshPercentageString)) {
                        boostRefreshPercentage.setValue(Integer.valueOf(boostRefreshPercentageString));
                    }
                    ClientUI.sendTrayNotification("MassFighter Settings Downloaded", TrayIcon.MessageType.INFO);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean save() {


        if (!selectedMonsters.getItems().isEmpty()) {
            AbstractScript script = Environment.getScript();
            if (script != null) {
                ManagedProperties managedProperties = Environment.getScript().getSettings();
                if (managedProperties != null) {
                    /* User Profile */
                    String npcString = String.join(",", selectedMonsters.getItems());
                    managedProperties.setProperty("npcNames", npcString);
                    //
                    String lootString = String.join(",", pickupLoot.getItems());
                    managedProperties.setProperty("lootNames", lootString);
                    //
                    String alchLootString = String.join(",", alchemyLoot.getItems());
                    managedProperties.setProperty("alchLoot", alchLootString);
                    //
                    String notepaperLootString = String.join(",", magicNotepaperLoot.getItems());
                    managedProperties.setProperty("notepaperLoot", notepaperLootString);
                    managedProperties.setProperty("targetSelection", Double.toString(targetSlider.getValue()));
                    managedProperties.setProperty("useFood", Boolean.toString(!foodSelection.getItems().isEmpty()));
                    managedProperties.setProperty("lootInCombat", Boolean.toString(lootInCombat.isSelected()));
                    managedProperties.setProperty("abilityMode", Integer.toString(abilitiesMode.getSelectionModel().getSelectedIndex()));
                    managedProperties.setProperty("useSoulsplit", Boolean.toString(soulsplit.isSelected()));
                    managedProperties.setProperty("foodLogout", Boolean.toString(logoutFood.isSelected()));
                    managedProperties.setProperty("foodTeleport", Boolean.toString(teleportFood.isSelected()));
                    managedProperties.setProperty("hitpointsLogout", Boolean.toString(logoutHitpoints.isSelected()));
                    managedProperties.setProperty("hitpointsTeleport", Boolean.toString(teleportHitpoints.isSelected()));
                    managedProperties.setProperty("buryBones", Boolean.toString(buryBones.isSelected()));
                    managedProperties.setProperty("quickPray", Boolean.toString(quickPray.isSelected()));
                    managedProperties.setProperty("attackCombatMonsters", Boolean.toString(attackCombatMonsters.isSelected()));
                    managedProperties.setProperty("bypassReachable", Boolean.toString(bypassReachable.isSelected()));
                    managedProperties.setProperty("lootByValue", Boolean.toString(isNumeric(lootValue.toString())));
                    managedProperties.setProperty("equipAmmunition", Boolean.toString(reequipAmmunition.isSelected()));
                    managedProperties.setProperty("soulsplitPermanent", Boolean.toString(soulsplitPerm.isSelected()));
                    managedProperties.setProperty("soulsplitPercentage", Double.toString(soulsplitPercentage.getValue()));
                    managedProperties.setProperty("lootValue", lootValue.getText());
                    managedProperties.setProperty("tagSelection", Double.toString(tagSlider.getValue()));
                    //
                    String foodString = String.join(",", foodSelection.getItems());
                    managedProperties.setProperty("foodNames", foodString);
                    //
                    managedProperties.setProperty("fightRadius", tileRange.getText());
                    managedProperties.setProperty("eatValue", Double.toString(eatValue.getValue()));
                    managedProperties.setProperty("prayValue", Double.toString(prayValue.getValue()));
                    managedProperties.setProperty("criticalHitpoints", criticalHitpoints.getText());
                    //
                    String potionString = String.join(",", selectedBoosts.getItems());
                    managedProperties.setProperty("selectedPotions", potionString);
                    //
                    managedProperties.setProperty("boostRefreshPercentage", Double.toString(boostRefreshPercentage.getValue()));
                    ClientUI.sendTrayNotification("MassFighter Settings Saved", TrayIcon.MessageType.INFO);
                    return true;
                }
            }
        }
        System.out.println("Could not save settings at this time");
        return false;
    }

    private Boolean isNumeric(String string) {
        String numericRegex = "\\d+";
        return string.matches(numericRegex);
    }

    private void closeUI() {
        Stage stage = Main.stage;
        if (stage != null && stage.isShowing()) {
            stage.close();
        }
    }

}
