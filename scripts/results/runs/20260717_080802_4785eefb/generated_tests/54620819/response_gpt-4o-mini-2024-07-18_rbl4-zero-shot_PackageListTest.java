package com.igormaznitsa.mvngolang.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.igormaznitsa.mvngolang.utils.PackageList;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class PackageListTest {

    private PackageList.ContentProvider contentProvider;
    private File mockFile;

    @Before
    public void setUp() {
        contentProvider = mock(PackageList.ContentProvider.class);
        mockFile = new File("mockFilePath");
    }

    @Test
    public void testPackageListConstructorValidInput() throws IOException, ParseException {
        String inputText = "package: com.example.package, branch: main\n" +
                           "#include \"includedFile.txt\"\n" +
                           "package: com.example.anotherpackage, tag: v1.0\n";
        
        when(contentProvider.readContent(any(File.class))).thenReturn("package: included.package, revision: r1.0");

        PackageList packageList = new PackageList(mockFile, inputText, contentProvider);
        List<PackageList.Package> packages = packageList.getPackages();

        assertEquals(3, packages.size());
        assertEquals("com.example.package", packages.get(0).getPackage());
        assertEquals("main", packages.get(0).getBranch());
        assertEquals("com.example.anotherpackage", packages.get(1).getPackage());
        assertEquals("v1.0", packages.get(1).getTag());
        assertEquals("included.package", packages.get(2).getPackage());
        assertEquals("r1.0", packages.get(2).getRevision());
    }

    @Test(expected = ParseException.class)
    public void testPackageListConstructorInvalidInput() throws IOException, ParseException {
        String inputText = "invalid input line\n";
        new PackageList(mockFile, inputText, contentProvider);
    }

    @Test
    public void testRemoveComment() {
        String textWithComment = "some text // this is a comment";
        String result = PackageList.removeComment(textWithComment, false);
        assertEquals("some text ", result);
    }

    @Test
    public void testRemoveQuotes() {
        String quotedText = "\"quoted text\"";
        String result = PackageList.removeQuotes(quotedText);
        assertEquals("quoted text", result);
        
        String unquotedText = "unquoted text";
        result = PackageList.removeQuotes(unquotedText);
        assertEquals("unquoted text", result);
    }

    @Test
    public void testPackageMakeString() {
        PackageList.Package pkg = new PackageList.Package("com.example.package", "main", "v1.0", "r1.0");
        String result = pkg.makeString();
        assertEquals("package: com.example.package,branch: main,tag: v1.0,revision: r1.0", result);
    }

    @Test
    public void testDoesNeedCvsProcessing() {
        PackageList.Package pkgWithBranch = new PackageList.Package("com.example.package", "main", null, null);
        assertTrue(pkgWithBranch.doesNeedCvsProcessing());

        PackageList.Package pkgWithoutBranch = new PackageList.Package("com.example.package", null, null, null);
        assertFalse(pkgWithoutBranch.doesNeedCvsProcessing());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPackageConstructorInvalidKey() throws ParseException {
        new PackageList.Package("invalidKey: value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPackageConstructorMissingPackage() throws ParseException {
        new PackageList.Package("branch: main");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPackageConstructorEmptyPackage() throws ParseException {
        new PackageList.Package("package: ");
    }
}
