package astra.gui;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

public class Astra extends Application
{
    private String tlePath;
    private final String WINDOW_TITLE = "Astra";
    
    private AstraController controller;
    
    public static void main(String[] args)
    {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception
    {
        // get the path of the TLE file
        List<String> params = this.getParameters().getRaw();
        
        try
        {
            this.tlePath = params.get(0);
        }
        catch (IndexOutOfBoundsException e)
        {
            System.err.println("Usage: astra tle_file");
        }
        

        // initialise controller
        this.controller = new AstraController();
        
        // initialise model
        AstraModel model = null;
        
        try
        {
           model = new AstraModel(this.tlePath);
           this.controller.setModel(model);
        }
        catch (Exception e)
        {
            this.controller.pushError(e);
        }
        
        // initialise view
        AstraView view = new AstraView();
        this.controller.setView(view);
        
        stage.setScene(view.getScene());
        stage.setTitle(this.WINDOW_TITLE);
        stage.show();
    }
}
