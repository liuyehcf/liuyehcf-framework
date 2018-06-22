package org.liuyehcf.compile.engine.hua.test;

/**
 * @author hechenfeng
 * @date 2018/6/1
 */
class TestCases {
    static final String[] CLASSIC_CASES = {
            "void sort(int[] nums, int size) {\n" +
                    "        sort(nums, 0, size-1);\n" +
                    "    }\n" +
                    "\n" +
                    "    void sort(int[] nums, int lo, int hi) {\n" +
                    "        if (lo < hi) {\n" +
                    "            int mid = partition(nums, lo, hi);\n" +
                    "            sort(nums, lo, mid - 1);\n" +
                    "            sort(nums, mid + 1, hi);\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    int partition(int[] nums, int lo, int hi) {\n" +
                    "        int i = lo - 1;\n" +
                    "        int pivot = nums[hi];\n" +
                    "\n" +
                    "        for (int j = lo; j < hi; j++) {\n" +
                    "            if (nums[j] < pivot) {\n" +
                    "                exchange(nums, ++i, j);\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        exchange(nums, ++i, hi);\n" +
                    "\n" +
                    "        return i;\n" +
                    "    }\n" +
                    "\n" +
                    "    void exchange(int[] nums, int i, int j) {\n" +
                    "        int temp = nums[i];\n" +
                    "        nums[i] = nums[j];\n" +
                    "        nums[j] = temp;\n" +
                    "    }"
    };
}
