package astra.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import astra.Satellite;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * View component of the Astra application
 * 
 */
public class AstraView
{
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    
    private ObservableList<Satellite> satellites;
    private ObservableList<Entry<String, String>> satelliteDescription;
    
    private BorderPane root;
    private ListView<Satellite> satelliteListDisplay;
    private TableView<Entry<String, String>> satelliteDescriptionDisplay;
    private Alert errorAlert;
    
    /**
     * Constructor for the {@link AstraView} class
     * 
     */
    public AstraView()
    {
        this.root = new BorderPane();
        this.satellites = FXCollections.observableArrayList();
        this.satelliteDescription = FXCollections.observableArrayList();
        
        this.addSatelliteListDisplay();
        this.addSatelliteDescriptionDisplay();
    }
    
    /**
     * @return satellite list
     */
    public List<Satellite> getSatelliteList()
    {
        return this.satellites;
    }
    
    /**
     * @param list
     *            satellite list
     */
    public void setSatelliteList(List<Satellite> list)
    {
        if(list == null)
        {
            throw new NullPointerException();
        }
        
        this.satellites.clear();
        this.satellites.addAll(list);
    }
    
    /**
     * @return root JavaFX node
     * 
     */
    public BorderPane getRoot()
    {
        return this.root;
    }
    
    /**
     * @return JavaFX scene
     * 
     */
    public Scene getScene()
    {
        return new Scene(this.getRoot(), this.WIDTH, this.HEIGHT);
    }
    
    /**
     * @return selected satellite
     * 
     */
    public Satellite getSelectedSatellite()
    {
        Satellite selectedSatellite = this.satelliteListDisplay
                        .getSelectionModel().getSelectedItem();
        
        // default to top of satellite list
        if(selectedSatellite == null)
        {
            selectedSatellite = this.satellites.get(0);
        }
        
        return selectedSatellite;
    }
    
    /**
     * @param description
     *            satellite description
     * 
     */
    public void setSatelliteDescription(Map<String, String> description)
    {
        if(description == null)
        {
            throw new NullPointerException();
        }
        
        List<Entry<String, String>> entryList = new ArrayList<
                        Entry<String, String>>(description.entrySet());
        Collections.reverse(entryList); // cheap hack to get ordering right
        
        this.satelliteDescription = FXCollections.observableArrayList(
                        entryList);
        this.satelliteDescriptionDisplay.setItems(this.satelliteDescription);
    }
    
    /**
     * Adds the satellite list display to the GUI
     * 
     */
    private void addSatelliteListDisplay()
    {
        this.satelliteListDisplay = new ListView<Satellite>();
        this.setupSatelliteListDisplay();
        this.satelliteListDisplay.getSelectionModel().select(0); // default
        this.satelliteListDisplay.setItems(this.satellites);
        this.root.setLeft(this.satelliteListDisplay);
    }
    
    /**
     * Sets up the entries of the {@link ListView} storing the satellite list
     * 
     */
    private void setupSatelliteListDisplay()
    {
        this.setupListSelectionListening(); // selection listening
    }
    
    /**
     * Sets up event listener for list selection. This avoids using a mouse
     * event handler (which clobbers list entry highlighting).
     * 
     */
    private void setupListSelectionListening()
    {
        this.satelliteListDisplay.setCellFactory((list) -> {
            return new ListCell<Satellite>()
            {
                @Override
                protected void updateItem(Satellite item, boolean empty)
                {
                    super.updateItem(item, empty);
                    
                    if(item == null || empty)
                    {
                        setText(null);
                    }
                    else
                    {
                        setText(item.getName());
                    }
                }
            };
        });
    }
    
    /**
     * Adds the satellite description display to the GUI
     * 
     */
    private void addSatelliteDescriptionDisplay()
    {
        this.satelliteDescriptionDisplay = new TableView<Entry<String, String>>(
                        this.satelliteDescription);
        this.setupSatelliteDescriptionDisplay();
        this.root.setRight(this.satelliteDescriptionDisplay);
    }
    
    /**
     * Sets up the columns of the {@link TableView} storing the satellite
     * description data
     * 
     */
    private void setupSatelliteDescriptionDisplay()
    {
        TableColumn<Entry<String, String>, String> keyColumn = new TableColumn<>(
                        "Field");
        TableColumn<Entry<String, String>, String> valueColumn = new TableColumn<>(
                        "Value");
        
        // field factories
        keyColumn.setCellFactory(column -> {
            return new KeyCell();
        });
        
        keyColumn.setCellValueFactory(cell -> {
            return new ReadOnlyObjectWrapper<>(cell.getValue().getKey());
        });
        
        // value factories
        valueColumn.setCellFactory(column -> {
            return new ValueCell();
        });
        
        valueColumn.setCellValueFactory(cell -> {
            return new ReadOnlyObjectWrapper<>(cell.getValue().getValue());
        });
        
        // add TableColumns to our TableView
        this.satelliteDescriptionDisplay.getColumns().add(keyColumn);
        this.satelliteDescriptionDisplay.getColumns().add(valueColumn);
    }
    
    private class KeyCell extends TableCell<Entry<String, String>, String>
    {
        @Override
        protected void updateItem(String key, boolean empty)
        {
            if(empty || key == null)
            {
                setText(null);
                setGraphic(null);
            }
            else
            {
                setText(key);
            }
        }
    }
    
    private class ValueCell extends TableCell<Entry<String, String>, String>
    {
        @Override
        protected void updateItem(String value, boolean empty)
        {
            if(empty || value == null)
            {
                setText(null);
                setGraphic(null);
            }
            else
            {
                setText(value);
            }
        }
    }
    
    /**
     * Adds <code>handler</code> as the event handler for satellite selection
     * events. These events are fired whenever the user clicks on an entry in
     * the satellite list display.
     * 
     * @param handler
     *            the {@link ChangeListener} for handling the selection
     */
    public void addSatelliteSelectionHandler(ChangeListener<Satellite> handler)
    {
        if(handler == null)
        {
            throw new NullPointerException();
        }
        
        this.satelliteListDisplay.getSelectionModel().selectedItemProperty()
                        .addListener(handler);
    }
    
    /**
     * Informs the user of an error in the application via an {@link Alert}
     * 
     * @param e
     *            the exception being raised
     */
    public void raiseError(Exception e)
    {
        if(e == null)
        {
            throw new NullPointerException();
        }
        
        this.errorAlert = new Alert(AlertType.ERROR, e.getMessage());
        this.showErrorAlert();
    }
    
    /**
     * Displays the error alert dialog box
     * 
     */
    private void showErrorAlert()
    {
        this.errorAlert.showAndWait();
    }
    
}
