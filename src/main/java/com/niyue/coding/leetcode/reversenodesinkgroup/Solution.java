package com.niyue.coding.leetcode.reversenodesinkgroup;

import com.niyue.coding.leetcode.common.ListNode;

// http://leetcode.com/onlinejudge#question_25
public class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        if(k > 1) {
            ListNode current = head;
            ListNode prev = null;
            int count = 0;
            while(current != null) {
                count++;
                ListNode next = current.next;
                if(count == k) {
                    ListNode groupStart = null;
                    
                    if(prev == null) {
                    	groupStart = head;
                        head = current;
                    } else {
                    	groupStart = prev.next;
                    	prev.next = current;
                    }
                    
                    reverse(groupStart, current, null);
                    groupStart.next = next;
                    prev = groupStart;
                    count = 0;
                }
                current = next;
            }
        }
        return head;        
    }

    private void reverse(ListNode start, ListNode end, ListNode prev) {
        ListNode next = null;
        end.next = null;
        for(ListNode current = start; current != null; current = next) {
            next = current.next;
            current.next = prev;
            prev = current;
        }
    }
}
