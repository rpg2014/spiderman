package com.rpg2014.spiderman.parsers;
import java.util.ArrayList;
import java.util.Map;

import com.rpg2014.spiderman.GroupMe.GroupMeCallback;
import com.rpg2014.spiderman.parsers.SpidermanParser;
import com.rpg2014.spiderman.types.SpidermanCommand;

import java.util.AbstractMap;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.easymock.EasyMock.*;
import org.easymock.*;
import junit.*;



public class SpidermanParserTest extends TestCase{
    
    public SpidermanParserTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( SpidermanParserTest.class );
    }
    
    public void testGetCommandEntryView2Names(){
        
        GroupMeCallback callback = mock(GroupMeCallback.class);
        expect(callback.getText()).andReturn("@spiderman view: name1, name2, name3").times(2);
        List<String> expectedList = new ArrayList<>();
        expectedList.add("name1");
        expectedList.add("name2")    ;
        expectedList.add("name3");
        Map.Entry<SpidermanCommand,List<String>> expected = new AbstractMap.SimpleEntry<SpidermanCommand,List<String>>(SpidermanCommand.VIEW, expectedList);
        replay(callback);
        Map.Entry<SpidermanCommand,List<String>> actual = SpidermanParser.getCommandEntry(callback);
        
        List<String> actualList = actual.getValue();
        for(int i=0;i<expectedList.size();i++) {
        	assertEquals(expectedList.get(i),actualList.get(i));
        }
    }
}

