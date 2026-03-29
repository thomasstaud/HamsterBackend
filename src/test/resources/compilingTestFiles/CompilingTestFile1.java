public class CompilingTestFile1 {
    public void testFoo() {
        System.out.println("FOO");
        CompilingTestFile2 compilingTestFile2 = new CompilingTestFile2();
        compilingTestFile2.doSth();
    }
}