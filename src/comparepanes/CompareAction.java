/*
 * $File: //depot/src/release/Tools/ComparePanesPlugin/src/com/liquidnet/ideaplugin/comparepanes/CompareAction.java $ $Revision: #7 $ $DateTime: 2004/10/13 14:53:47 $
 *
 * Compare Panes plugin for IDEA: compare 2 panes from caret to end.
 *
 * Copyright (c) 2003, Liquidnet Holdings, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   -> Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *   -> Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *   -> Neither the name of Liquidnet Holdings, Inc. nor the names of its
 *      contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package comparepanes;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class CompareAction extends AnAction {

    private static App _app;

    static void setApp(App app) {
        _app = app;
    }

    public void update(AnActionEvent e) {
        Project project = (Project)e.getDataContext().getData(DataConstants.PROJECT);
        Proj proj = (Proj)project.getComponent(Proj.class);
        Editor current = proj.getCurrentEditor();
        Editor previous = proj.getPreviousEditor();
        e.getPresentation().setEnabled(previous != null && current != null);
    }

    public void actionPerformed(AnActionEvent e) {
        if (_app.isAsk()) {
            Project project = (Project)e.getDataContext().getData(DataConstants.PROJECT);
            boolean ok = new OptionsDialog(project, _app).showDialog();
            if (ok == false) {
                return;
            }
        }
        boolean ignoreWhitespace = _app.isIgnoreWhitespace();
        Project project = (Project)e.getDataContext().getData(DataConstants.PROJECT);
        Proj proj = (Proj)project.getComponent(Proj.class);
        Editor currentEditor = proj.getCurrentEditor();
        Editor previousEditor = proj.getPreviousEditor();
        if (previousEditor != null && currentEditor != null) {
            int currentPos = currentEditor.getSelectionModel().getSelectionStart();
            int previousPos = previousEditor.getSelectionModel().getSelectionStart();
            BufferedReader currentReader = getContentReader(currentEditor, currentPos);
            BufferedReader previousReader = getContentReader(previousEditor, previousPos);

            LogicalPosition currentLP0 = currentEditor.offsetToLogicalPosition(currentPos);
            LogicalPosition previousLP0 = previousEditor.offsetToLogicalPosition(previousPos);
            LineState currentEditorLine = new LineState(currentReader, currentLP0);
            LineState previousEditorLine = new LineState(previousReader, previousLP0);
            while (true) {

                if (currentEditorLine.currentLine == null && previousEditorLine.currentLine == null) {
                    break;
                }

                if (currentEditorLine.currentLine != null) {
                    if (ignoreWhitespace && currentEditorLine.currentLine.length() == 0) {
                        // trailing blank lines
                        currentEditorLine.nextLine();
                        continue;
                    }
                    else if (previousEditorLine.currentLine == null) {
                        break;
                    }
                }
                if (previousEditorLine.currentLine != null) {
                    if (ignoreWhitespace && previousEditorLine.currentLine.length() == 0) {
                        // trailing blank lines
                        previousEditorLine.nextLine();
                    }
                    else if (currentEditorLine.currentLine == null) {
                        break;
                    }
                }
                else {
                    break;
                }

                // If we get here, neither editor is done yet, and both lines are non-null.

                // Short-circuit.
                if (currentEditorLine.currentLine.equals(previousEditorLine.currentLine)) {
                    currentEditorLine.nextLine();
                    previousEditorLine.nextLine();
                    continue;
                }
                int currentLen = currentEditorLine.currentLine.length();
                int previousLen = previousEditorLine.currentLine.length();
                int currentIndex = currentEditorLine.currentPosition.column;
                int previousIndex = previousEditorLine.currentPosition.column;

                while(currentIndex < currentLen || previousIndex < previousLen) {

                    char cc = 0; // Make compiler happy.
                    char cp;

                    if (currentIndex < currentLen) {
                        cc = currentEditorLine.currentLine.charAt(currentIndex);
                        if (ignoreWhitespace && Character.isWhitespace(cc)) {
                            currentIndex++;
                            continue;
                        }
                        else if (previousIndex >= previousLen) {
                            break;
                        }
                    }
                    if (previousIndex < previousLen) {
                        cp = previousEditorLine.currentLine.charAt(previousIndex);
                        if (ignoreWhitespace && Character.isWhitespace(cp)) {
                            previousIndex++;
                            continue;
                        }
                        else if (currentIndex >= currentLen) {
                            break;
                        }
                    }
                    else {
                        break;
                    }

                    // If we get here, neither line is done yet, and both cc and cp are set.
                    if (cp != cc) {
                        break;
                    }
                    else {
                        previousIndex++;
                        currentIndex++;
                    }
                }

                // Lines are only the same if both reached end.
                boolean diff = false;
                if (currentIndex < currentLen) {
                    currentEditorLine.moveTo(currentIndex);
                    diff = true;
                }
                else {
                    currentEditorLine.nextLine();
                }
                if (previousIndex < previousLen) {
                    previousEditorLine.moveTo(previousIndex);
                    diff = true;
                }
                else {
                    previousEditorLine.nextLine();
                }
                if (diff) {
                    break;
                }
            }

            currentPos = currentEditor.logicalPositionToOffset(currentEditorLine.currentPosition);
            previousPos = previousEditor.logicalPositionToOffset(previousEditorLine.currentPosition);
            if (previousPos == previousEditor.getDocument().getTextLength() && currentPos == currentEditor.getDocument().getTextLength()) {
                Messages.showMessageDialog(project, "No differences found from selections to end.", "No Differences", Messages.getInformationIcon());
            }
            else {
                moveTo(previousEditor, previousPos);
                moveTo(currentEditor, currentPos);
            }
        }
    }

    private static BufferedReader getContentReader(Editor currentEditor, int currentPos) {
        Document doc = currentEditor.getDocument();
        String selectionToEnd = doc.getText().substring(currentPos);
        return new BufferedReader(new StringReader(selectionToEnd));
    }

    private void moveTo(Editor editor, int pos) {
        int len = editor.getDocument().getTextLength();
        if (pos < len) {
            editor.getSelectionModel().setSelection(pos, pos + 1);
            editor.getCaretModel().moveToOffset(pos + 1);
        }
        else {
            editor.getSelectionModel().setSelection(pos, pos);
            editor.getCaretModel().moveToOffset(pos);
        }
        editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
    }

    private static class LineState {
        private BufferedReader reader;
        public String currentLine;
        public LogicalPosition currentPosition;

        public LineState(BufferedReader reader, LogicalPosition currentPosition) {
            this.reader = reader;
            this.currentPosition = currentPosition;
            read();
        }

        public void nextLine() {
            currentPosition = new LogicalPosition(currentPosition.line + 1, 0);
            read();
        }

        private void read() {
            try {
                currentLine = reader.readLine();
            }
            catch(IOException x) {
                // Never happens
                currentLine = null;
            }
        }

        public void moveTo(int position) {
            currentPosition = new LogicalPosition(currentPosition.line, position);

        }
    }
}
