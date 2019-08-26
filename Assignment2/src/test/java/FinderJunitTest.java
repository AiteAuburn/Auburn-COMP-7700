import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FinderJunitTest {

    @Test
    public void test_findMin_emptyArray(){
        Finder f = new Finder();
        int[] arr = new int[]{};
        Integer expectedResult = null;
        Integer actualResult = f.findMin(arr);
        assertEquals(expectedResult,actualResult);
    }
    @Test
    public void test_findMin_invalidArray(){
        Finder f = new Finder();
        Integer expectedResult = null;
        Integer actualResult = f.findMin(null);
        assertEquals(expectedResult,actualResult);
    }
    @Test
    public void test_findMin_validArray(){
        Finder f = new Finder();
        int[] arr = new int[]{1,2,3,4,5,6,7,8,9,10};
        Integer expectedResult = 1;
        Integer actualResult = f.findMin(arr);
        assertEquals(expectedResult,actualResult);
    }


    @Test
    public void test_findMax_emptyArray(){
        Finder f = new Finder();
        int[] arr = new int[]{};
        Integer expectedResult = null;
        Integer actualResult = f.findMax(arr);
        assertEquals(expectedResult,actualResult);
    }
    @Test
    public void test_findMax_invalidArray(){
        Finder f = new Finder();
        Integer expectedResult = null;
        Integer actualResult = f.findMax(null);
        assertEquals(expectedResult,actualResult);
    }
    @Test
    public void test_findMax_validArray(){
        Finder f = new Finder();
        int[] arr = new int[]{1,2,3,4,5,6,7,8,9,10};
        Integer expectedResult = 10;
        Integer actualResult = f.findMax(arr);
        assertEquals(expectedResult,actualResult);
    }
}
