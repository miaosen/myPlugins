package test;

import com.intellij.navigation.NavigationItem;

public interface StringElement extends NavigationItem {

    String getName();

    String getTag();

    String getValue();

    String getParentDirName();

}
