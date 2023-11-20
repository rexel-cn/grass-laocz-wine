package com.rexel.common.utils;

import java.util.Arrays;

/**
 * @ClassName LongUtils
 * @Description
 * @Author 孟开通
 * @Date 2022/10/13 16:29
 **/
public class LongUtils {
    /**
     * 交集
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public static Long[] intersect(Long[] nums1, Long[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int length1 = nums1.length, length2 = nums2.length;
        Long[] intersection = new Long[Math.min(length1, length2)];
        int index1 = 0, index2 = 0, index = 0;
        while (index1 < length1 && index2 < length2) {
            if (nums1[index1] < nums2[index2]) {
                index1++;
            } else if (nums1[index1] > nums2[index2]) {
                index2++;
            } else {
                intersection[index] = nums1[index1];
                index1++;
                index2++;
                index++;
            }
        }
        return Arrays.copyOfRange(intersection, 0, index);
    }
}
