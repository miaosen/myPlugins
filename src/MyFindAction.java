import com.intellij.find.*;
import com.intellij.find.impl.livePreview.LivePreviewController;
import com.intellij.find.impl.livePreview.SearchResults;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.EventDispatcher;

import java.util.LinkedList;
import java.util.regex.PatternSyntaxException;

/**
 * Created by OAIM on 2016/8/14.
 */
public class MyFindAction extends AnAction {


    private Project myProject;

    private SearchResults mySearchResults;

    private LivePreviewController myLivePreviewController;

    private final EventDispatcher<SearchReplaceComponent.Listener> myEventDispatcher = EventDispatcher.create(SearchReplaceComponent.Listener.class);

    private LinkedList<FindResult> results = new LinkedList<>();

    private AnActionEvent e;

    private Editor mEditor;


    FindComponent myComponent;

    FindPanel findPanel;

    int scrollIndex=0;


    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        e = anActionEvent;
        myProject = anActionEvent.getProject();
        mEditor = e.getData(PlatformDataKeys.EDITOR);
        Application application = ApplicationManager.getApplication();
        myComponent = application.getComponent(FindComponent.class);
        myComponent.setMyFindAction(this);
        String selectWord = getSelectWord(anActionEvent);
        startSearch(selectWord);
        srcollTo();
        myComponent.showDialog(selectWord);

    }

    /**
     * 开始搜索
     *
     * @param selectWord
     */
    public void startSearch(String selectWord) {
        scrollIndex=0;
        if(selectWord!=null&&selectWord.length()>0){
            FindManager findManager = FindManager.getInstance(myProject);
            FindModel findModel = new FindModel();
            findModel.copyFrom(findManager.getFindInFileModel());
            findModel.setStringToFind(selectWord);
            // System.out.println("findModel==========" + findModel.toString());
            findInRange(new TextRange(0, Integer.MAX_VALUE), mEditor, findModel, results);
        }

    }


    /**
     * 搜索的基础方法
     *
     * @param r
     * @param editor
     * @param findModel
     * @param results
     */
    private void findInRange(TextRange r, Editor editor, FindModel findModel, LinkedList<FindResult> results) {
        results.clear();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        CharSequence charSequence = editor.getDocument().getCharsSequence();
        int offset = r.getStartOffset();
        int maxOffset = Math.min(r.getEndOffset(), charSequence.length());
        FindManager findManager = FindManager.getInstance(myProject);

        while (true) {
            FindResult result;
            try {
                CharSequence bombedCharSequence = StringUtil.newBombedCharSequence(charSequence, 3000);
                result = findManager.findString(bombedCharSequence, offset, findModel, virtualFile);
                System.out.println("result================" + result);
            } catch (PatternSyntaxException e) {
                result = null;
            } catch (ProcessCanceledException e) {
                result = null;
            }
            if (result == null || !result.isStringFound()) break;
            int newOffset = result.getEndOffset();
            if (result.getEndOffset() > maxOffset) break;
            if (offset == newOffset) {
                if (offset < maxOffset - 1) {
                    offset++;
                } else {
                    results.add(result);
                    break;
                }
            } else {
                offset = newOffset;
            }
            results.add(result);
        }
    }


    /**
     * 获取选中的文字
     *
     * @param e
     * @return
     */
    private String getSelectWord(AnActionEvent e) {
        Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        SelectionModel model = mEditor.getSelectionModel();
        String selectedText = model.getSelectedText();
        System.out.println("selectedText==========" + selectedText);
        return selectedText;
    }


    public void srcollTo() {
        if(results.size()>1){
            FindResult findResult=results.get(scrollIndex);
            final Document doc = mEditor.getDocument();
            int lineNumber = doc.getLineNumber(findResult.getStartOffset());
            System.out.println("lineNumber==========" + lineNumber);
            LogicalPosition logicalPosition = new LogicalPosition(lineNumber, 0);
            ScrollingModel scrollingModel = mEditor.getScrollingModel();

            scrollingModel.scrollTo(logicalPosition, ScrollType.MAKE_VISIBLE);

            if(scrollIndex==results.size()-1){
                scrollIndex=0;
            }else{
                scrollIndex=scrollIndex+1;
            }
            SelectionModel selectionModel=mEditor.getSelectionModel();
            selectionModel.setSelection(findResult.getStartOffset(),findResult.getEndOffset());

        }

    }


    public int getScrollIndex() {
        return scrollIndex;
    }

    public void setScrollIndex(int scrollIndex) {
        this.scrollIndex = scrollIndex;
    }
}
