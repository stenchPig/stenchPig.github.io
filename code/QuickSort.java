package code;

/**
 * 快排
 **/
public class QuickSort {

    public static int partiti6on(int[] nums, int left, int right) {
        int p = nums[left];
        int i = left;
        int j = right;
        while (i < j) {
            while (p <= nums[j] && i < j) {
                j--;
            }
            nums[i] = nums[j];
            while (p >= nums[i] && i < j) {
                i++;
            }
            nums[j] = nums[i];
        }
        nums[i] = p;
        return i;
    }

    /**
     * 快排
     */
    public static void qSort(int[] nums, int left, int right) {
        if (left < right) {
            int p = partition(nums, left, right);
            qSort(nums, left, p - 1);
            qSort(nums, p + 1, right);
        }
    }

    /**
     * 快排分割 - target左边的数都比target小, target右边的数都比target大
     */
    public static void topK(int[] nums, int left, int right, int target) {
        if (left < right) {
            int p = partition(nums, left, right);
            if (target < p) {
                topK(nums, left, p - 1, target);
            } else if (target > p){
                topK(nums, p + 1,  right, target);
            }
        }
    }
}
