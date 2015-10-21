package com.disney.wdpr.jenkins.dto.report;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.disney.wdpr.jenkins.dto.extract.comments.Comment;

public class CommentTest {

    @Test
    public void testCompareTo() {
        final String userId_1= "00702361";
        final String userId_2= "user2";
        
        final String discussionId_1 ="bdXWcH9ZnfFzbnRplfiavL";
        final String discussionId_2 ="dis2";
        
        final String lastMod_1 ="01-29-2015:07:24AM";
        final String lastMod_2 ="01-22-2015:08:06AM";
        final String lastMod_3 ="12-22-2015:08:06AM";
        
        final Calendar cal1 = new GregorianCalendar(2014,12,25);
        final Calendar cal2 = new GregorianCalendar(2015,1,25);
        final Calendar cal3 = new GregorianCalendar(2015,2,22);
        final Calendar cal4 = new GregorianCalendar(2014,1,25);
        
        final Comment comment_A = new Comment();
        comment_A.setUserId(userId_1);
        comment_A.setDiscussionId(discussionId_1);
        comment_A.setLastModified("Date("+String.valueOf(cal1.getTimeInMillis())+")");
    
        final Comment comment_B = new Comment();
        comment_B.setUserId(userId_1);
        comment_B.setDiscussionId(discussionId_1);
        comment_B.setLastModified("Date("+String.valueOf(cal2.getTimeInMillis())+")");

        final Comment comment_C = new Comment();
        comment_C.setUserId(userId_1);
        comment_C.setDiscussionId(discussionId_1);
        comment_C.setLastModified("Date("+String.valueOf(cal3.getTimeInMillis())+")");
        
        final Comment comment_D = new Comment();
        comment_D.setUserId(userId_1);
        comment_D.setDiscussionId(discussionId_2);
        comment_D.setLastModified("Date("+String.valueOf(cal4.getTimeInMillis())+")");
        
        final int test1 = comment_A.compareTo(comment_B);
        final int test2 = comment_A.compareTo(comment_C);
        final int test3 = comment_A.compareTo(comment_D);
        final int test4 = comment_B.compareTo(comment_D);
        
        
    }

}
