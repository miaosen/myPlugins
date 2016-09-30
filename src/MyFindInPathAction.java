//import com.intellij.execution.impl.ConsoleViewUtil;
//import com.intellij.find.EditorSearchSession;
//import com.intellij.find.FindManager;
//import com.intellij.find.FindModel;
//import com.intellij.find.FindUtil;
//import com.intellij.ide.DataManager;
//import com.intellij.openapi.actionSystem.CommonDataKeys;
//import com.intellij.openapi.actionSystem.DataContext;
//import com.intellij.openapi.editor.Editor;
//import com.intellij.openapi.editor.actionSystem.EditorAction;
//import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
//import com.intellij.openapi.editor.actions.IncrementalFindAction;
//import com.intellij.openapi.project.Project;
//
///**
// * Created by OAIM on 2016/9/26.
// */
//public class MyFindInPathAction extends IncrementalFindAction {
//
//    public MyFindInPathAction() {
//        super(new MyFindInPathAction.Handler(false));
//
//    }
//
//    public static class Handler extends EditorActionHandler {
//        private final boolean a;
//
//        public Handler(boolean var1) {
//            this.a = var1;
//        }
//
//        public void execute(Editor editor, DataContext dataContext) {
//            Project project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(editor.getComponent()));
//            if (!editor.isOneLineMode()) {
//                EditorSearchSession editorSearchSession = EditorSearchSession.get(editor);
//
//                if (editorSearchSession != null) {
//                    String textInField = editorSearchSession.getTextInField();
//                    System.out.println("textInField====" + textInField);
//                    editorSearchSession.getComponent().requestFocusInTheSearchFieldAndSelectContent(project);
//                    FindUtil.configureFindModel(this.a, editor, editorSearchSession.getFindModel(), false);
//                } else {
//                    FindManager findManager = FindManager.getInstance(project);
//                    FindModel findModel;
//                    if (this.a) {
//                        findModel = findManager.createReplaceInFileModel();
//                    } else {
//                        findModel = new FindModel();
//                        findModel.copyFrom(findManager.getFindInFileModel());
//                    }
//
//                    FindUtil.configureFindModel(this.a, editor, findModel, true);
//                    EditorSearchSession.start(editor, findModel, project).getComponent().requestFocusInTheSearchFieldAndSelectContent(project);
//
//                }
//            }
//
//        }
//
//        public boolean isEnabled(Editor var1, DataContext var2) {
//            if (this.a && ConsoleViewUtil.isConsoleViewEditor(var1)) {
//                return false;
//            } else {
//                Project var3 = (Project) CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(var1.getComponent()));
//                return var3 != null && !var1.isOneLineMode();
//            }
//        }
//    }
//
//}
//
