import com.intellij.ide.util.TipAndTrickBean;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.containers.ContainerUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by OAIM on 2016/9/4.
 */
public class FindPanel extends JPanel {


    private static final int e = 200;
    private static final int d = 600;
    private final java.util.List<TipAndTrickBean> b = ContainerUtil.newArrayList();

    public GridBagLayout layout ;

    OnSearchTextChangeListener onSearchTextChangeListener;

    JLabel jlFind;
    JEditorPane jeFind;

    MyFindAction myFindAction;

    public FindPanel( MyFindAction myFindAction) {
        this.myFindAction=myFindAction;
        initLayout();
    }

    public void setSearchText(String text){
        jeFind.setText(text);
        System.out.println("setSearchText==========="+text);
    }

    private void initLayout() {
        layout = new GridBagLayout();
        this.setLayout(layout);
         jlFind = new JLabel("查找:");
         jeFind= new JEditorPane();

        jeFind.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                myFindAction.startSearch(jeFind.getText());
            }
        });
        findOrReplaceStyle(jlFind,jeFind);
        nextLine();
        JLabel jlReplace = new JLabel("替换:");
        JEditorPane jeReplace= new JEditorPane();
        findOrReplaceStyle(jlReplace,jeReplace);

        nextLine();
        nextLine();
        nextLine();
        nextLine();

        JButton btnFind=new JButton("  查  找  ");
        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myFindAction.srcollTo();
            }
        });
        JButton btnRepalceAll=new JButton("替换全部");


        btnStyle(btnFind,btnRepalceAll);
    }

    public void nextLine(){
        JPanel jPanel = new JPanel();
        GridBagConstraints s = new GridBagConstraints();
        s.gridwidth = 0;
        layout.setConstraints(jPanel,s);
        this.add(jPanel);
    }


    public void findOrReplaceStyle(JLabel jLabel,JEditorPane jEditorPane) {
        GridBagConstraints s = new GridBagConstraints();
        //是用来控制添加进的组件的显示位置
        s.fill = GridBagConstraints.BOTH;
        jLabel.setFont(new Font("",Font.PLAIN,18));
        s.insets=new Insets(10,10,10,0);
        s.gridwidth = 1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx =1;
        layout.setConstraints(jLabel,s);

        this.add(jLabel);
        jEditorPane.setFont(new Font("",Font.PLAIN,18));
        s.weightx = 1;
        s.gridwidth = 2;
        s.insets=new Insets(10,0,10,0);
        layout.setConstraints(jEditorPane,s);
        this.add(jEditorPane);

    }


    public void btnStyle(JButton jButton1,JButton jButton2) {
        GridBagConstraints s = new GridBagConstraints();
        //是用来控制添加进的组件的显示位置
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth = 0;
        JPanel jPanel=new JPanel();
        layout.setConstraints(jPanel,s);
        this.add(jPanel);

        jButton1.setFont(new Font("",Font.PLAIN,18));
        s.insets=new Insets(10,10,10,10);
        s.gridwidth = 1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        layout.setConstraints(jButton1,s);
        jPanel.add(jButton1);


        jButton2.setFont(new Font("",Font.PLAIN,18));
        s.gridwidth = 1;
        s.insets=new Insets(10,0,10,10);
        layout.setConstraints(jButton2,s);
        jPanel.add(jButton2);

    }



    public interface OnSearchTextChangeListener{
       void ontextChange(String text);
    }


    public OnSearchTextChangeListener getOnSearchTextChangeListener() {
        return onSearchTextChangeListener;
    }

    public void setOnSearchTextChangeListener(OnSearchTextChangeListener onSearchTextChangeListener) {
        this.onSearchTextChangeListener = onSearchTextChangeListener;
    }
}
