class Finder {
    Integer findMin(int[] intArray) {
        int arraySize;
        if (intArray == null || (arraySize = intArray.length) == 0)
            return null;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arraySize; i++)
            min = Math.min(intArray[i],min);
        return min;
    }
    Integer findMax(int[] intArray) {
        int arraySize;
        if (intArray == null || (arraySize = intArray.length) == 0)
            return null;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arraySize; i++)
            max = Math.max(intArray[i],max);
        return max;
    }
}
