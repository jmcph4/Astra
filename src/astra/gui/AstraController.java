package astra.gui;

import java.util.Map;

import astra.Satellite;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Controller component of the Astra application
 * 
 * */
public class AstraController
{
    private AstraModel model;
    private AstraView view;
    
    /**
     * Constructor for the {@link AstraController} class
     * 
     * */
    public AstraController()
    {
        this.model = null;
        this.view = null;
    }
    
    /**
     * Constructor for the {@link AstraController} class
     * 
     * @param model
     *          the model for the application
     * @param view
     *          the view for the application
     * */
    public AstraController(AstraModel model, AstraView view)
    {
        if(model == null || view == null)
        {
            throw new NullPointerException();
        }
        
        this.model = model;
        this.view = view;
        
        this.updateView(); // push initial data to view
        
        this.setupEventHandlers();
    }
    
    /**
     * @param model
     *          model
     * 
     * */
    public void setModel(AstraModel model)
    {
        if(model == null)
        {
            throw new NullPointerException();
        }
        
        this.model = model;
    }
    
    /**
     * @param view
     *          view
     * */
    public void setView(AstraView view)
    {
        if(view == null)
        {
            throw new NullPointerException();
        }
        
        this.view = view;
        
        this.setupEventHandlers();
        this.updateView();
    }
    
    /**
     * Assigns appropriate event handlers to each action in the view
     * 
     * */
    private void setupEventHandlers()
    {
        // event handlers
        view.addSatelliteSelectionHandler(new SatelliteSelectionHandler());
    }
    
    /**
     * Updates the view by pulling new data from the model and pushing it to
     * the view
     * 
     * */
    public void updateView()
    {
        this.view.setSatelliteList(this.model.getSatelliteList());
        this.view.setSatelliteDescription(this.model.getSatelliteDescription(
                        this.view.getSelectedSatellite()));
    }
    
    private class SatelliteSelectionHandler implements ChangeListener<Satellite>
    {
        @Override
        public void changed(ObservableValue<? extends Satellite> arg0,
                        Satellite arg1, Satellite arg2)
        {
            Satellite selectedSatellite = view.getSelectedSatellite();
            Map<String, String> satelliteDescription =
                            model.getSatelliteDescription(selectedSatellite);
            view.setSatelliteDescription(satelliteDescription);
        }
    }

    /**
     * Pushes an error forward into the view
     * 
     * @param e
     *          the exception being raised
     * */
    public void pushError(Exception e)
    {
        if(e == null)
        {
            throw new NullPointerException();
        }
        
        this.view.raiseError(e);
    }
}
