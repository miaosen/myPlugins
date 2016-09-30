import com.intellij.ide.util.TipDialog;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by OAIM on 2016/8/14.
 */
public class FindComponent implements ApplicationComponent, Configurable {


    private Findialog findialog=null;

    FindPanel findPanel;

    MyFindAction myFindAction;


    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here

    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "FindComponent";
    }

    public void showDialog(String searchText) {
        if(findialog==null){
            findialog=new Findialog(myFindAction);
        }
        findPanel= findialog.getFindPanel();
        findialog.setSearchText(searchText);
        if(findialog.isShowing()){
            //TODO
        }else{
            findialog.show();
        }

    }


    public void testTipDialog(){
        TipDialog tipDialog=new TipDialog();
        tipDialog.setTitle("搜索");
        tipDialog.show();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return new JComponent() {

        };
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public void reset() {

    }

    @Override
    public void disposeUIResources() {

    }

    public Findialog getFindialog() {
        return findialog;
    }

    public void setFindialog(Findialog findialog) {
        this.findialog = findialog;
    }

    public FindPanel getFindPanel() {
        return findPanel;
    }

    public void setFindPanel(FindPanel findPanel) {
        this.findPanel = findPanel;
    }

    public MyFindAction getMyFindAction() {
        return myFindAction;
    }

    public void setMyFindAction(MyFindAction myFindAction) {
        this.myFindAction = myFindAction;
    }
}
