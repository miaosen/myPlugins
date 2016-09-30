import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.ex.WindowManagerEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author zengmiaosen
 * @email 1510809124@qq.com
 * @CreateDate 2016/9/29 11:57
 * @Descrition 搜索弹窗
 */
public class Findialog extends DialogWrapper {

    private FindPanel findPanel;

    //编辑区域获取的文字
    private String searchText;

    MyFindAction myFindAction;

    public Findialog(MyFindAction myFindAction) {
        super(WindowManagerEx.getInstanceEx().findVisibleFrame(), true);
        this.myFindAction = myFindAction;
        initPanel();
    }


    private void initPanel() {
        this.setModal(false);
        this.setTitle("查找/替换");
        Action action = getCancelAction();
        action.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        });
        this.setCancelButtonText("替换");
        this.setOKButtonText("查找");
        this.findPanel = new FindPanel(myFindAction);
        findPanel.setSearchText(searchText);
        this.setHorizontalStretch(1.00F);
        this.setVerticalStretch(1.00F);
        this.init();
    }

    @NotNull
    @Override
    protected Action getOKAction() {
        Action action=new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!");
            }
        };
        return action;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return findPanel;
    }

    public FindPanel getFindPanel() {
        return findPanel;
    }

    public void setFindPanel(FindPanel findPanel) {
        this.findPanel = findPanel;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        findPanel.setSearchText(searchText);
    }
}
