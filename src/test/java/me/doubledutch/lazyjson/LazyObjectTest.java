package me.doubledutch.lazyjson;

import java.io.File;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.*;

public class LazyObjectTest{
    @Test
    public void testRyansSample() throws LazyException{
        String str="{\"data\":{\"blah\":9},\"header\":{}}";
        LazyObject obj=new LazyObject(str);
        assertTrue(obj.has("header"));
        assertTrue(obj.has("data"));
        assertEquals(obj.toString(),str);
    }

    @Test
    public void testDeepNesting() throws LazyException{
        String str="{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":{\"foo\":42}}}}}}}}}}}}}}}}";
        LazyObject obj=new LazyObject(str);
        for(int i=0;i<15;i++){
            obj=obj.getJSONObject("foo");
            assertNotNull(obj);
        }
        assertEquals(42,obj.getInt("foo"));
    }

    @Test
    public void testHas() throws LazyException{
        String str="{\"foo\":\"bar\",\"baz\":{\"key\":42}}";
        LazyObject obj=new LazyObject(str);
        assertTrue(obj.has("foo"));
        assertFalse(obj.has("bar"));
        assertTrue(obj.has("baz"));
        assertFalse(obj.has("key"));
        assertFalse(obj.has("random"));
    }

     @Test
    public void testKeys() throws LazyException{
        String str="{\"foo\":\"bar\",\"baz\":{\"key\":42}}";
        LazyObject obj=new LazyObject(str);
        Iterator<String> it=obj.keys();
        assertTrue(it.hasNext());
        assertEquals("foo",it.next());
        assertTrue(it.hasNext());
        assertEquals("baz",it.next());
        assertFalse(it.hasNext());
    }

     @Test
    public void testLength() throws LazyException{
        String str="{\"foo\":\"bar\",\"baz\":{\"key\":42}}";
        LazyObject obj=new LazyObject(str);
        assertEquals(2,obj.length());
        str="{}";
        obj=new LazyObject(str);
        assertEquals(0,obj.length());
    }

    @Test
    public void testStringFields() throws LazyException{
        String str="{\"foo\":\"bar\",\"baz\":\"\"}";
        LazyObject obj=new LazyObject(str);
        String value=obj.getString("foo");
        assertNotNull(value);
        assertEquals(value,"bar");
        value=obj.getString("baz");
        assertNotNull(value);
        assertEquals(value,"");
    }

    @Test
    public void testIntegerFields() throws LazyException{
        String str="{\"foo\":999,\"bar\":0,\"baz\":42,\"bonk\":-378}";
        LazyObject obj=new LazyObject(str);
        assertEquals(999,obj.getInt("foo"));
        assertEquals(0,obj.getInt("bar"));
        assertEquals(42,obj.getInt("baz"));
        assertEquals(-378,obj.getInt("bonk"));
    }

    @Test
    public void testDoubleFields() throws LazyException{
        String str="{\"foo\":3.1415,\"bar\":0.0,\"baz\":1.2345e+1,\"bonk\":-3.78}";
        LazyObject obj=new LazyObject(str);
        assertEquals(3.1415d,obj.getDouble("foo"),0);
        assertEquals(0.0,obj.getDouble("bar"),0);
        assertEquals(12.345,obj.getDouble("baz"),0);
        assertEquals(-3.78,obj.getDouble("bonk"),0);
    }

    @Test
    public void testBooleanFields() throws LazyException{
        String str="{\"foo\":false,\"bar\":true}";
        LazyObject obj=new LazyObject(str);
        assertEquals(false,obj.getBoolean("foo"));
        assertEquals(true,obj.getBoolean("bar"));
    }

    @Test
    public void testNullFields() throws LazyException{
        String str="{\"foo\":null,\"bar\":42}";
        LazyObject obj=new LazyObject(str);
        assertEquals(true,obj.isNull("foo"));
        assertEquals(false,obj.isNull("bar"));
    }

     @Test
    public void testObjectSpaces() throws LazyException{
        String str=" {    \"foo\" :\"bar\" ,   \"baz\":  42}   ";
        LazyObject obj=new LazyObject(str);
        assertEquals("bar",obj.getString("foo"));
        assertEquals(42,obj.getInt("baz"));
    }

    @Test
    public void testObjectTabs() throws LazyException{
        String str="\t{\t\"foo\"\t:\"bar\"\t,\t\t\"baz\":\t42\t}\t";
        LazyObject obj=new LazyObject(str);
        assertEquals("bar",obj.getString("foo"));
        assertEquals(42,obj.getInt("baz"));
    }

    @Test
    public void testNestedObjects() throws LazyException{
        String str="{\"foo\":\"bar\",\"baz\":{\"test\":9}}";
        LazyObject obj=new LazyObject(str);
        obj=obj.getJSONObject("baz");
        assertNotNull(obj);
        assertEquals(9,obj.getInt("test"));
    }

    @Test
    public void testDeepNestedObjects() throws LazyException{
        String str="{\"foo\":\"bar\",\"baz\":{\"test\":9,\"test2\":{\"id\":100},\"second\":33}}";
        LazyObject obj=new LazyObject(str);
        obj=obj.getJSONObject("baz");
        assertNotNull(obj);
        assertEquals(9,obj.getInt("test"));
        obj=obj.getJSONObject("test2");
        assertNotNull(obj);
        assertEquals(100,obj.getInt("id"));
    }

    @Test
    public void testJSONOrgSample1() throws LazyException{
        String str="{\n    \"glossary\": {\n        \"title\": \"example glossary\",\n        \"GlossDiv\": {\n            \"title\": \"S\",\n            \"GlossList\": {\n                \"GlossEntry\": {\n                    \"ID\": \"SGML\",\n                    \"SortAs\": \"SGML\",\n                    \"GlossTerm\": \"Standard Generalized Markup Language\",\n                    \"Acronym\": \"SGML\",\n                    \"Abbrev\": \"ISO 8879:1986\",\n                    \"GlossDef\": {\n                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n                        \"GlossSeeAlso\": [\"GML\", \"XML\"]\n                    },\n                    \"GlossSee\": \"markup\"\n                }\n            }\n        }\n    }}";
        LazyObject obj=new LazyObject(str);
        LazyObject glo=obj.getJSONObject("glossary");
        assertNotNull(glo);
        assertEquals("example glossary",glo.getString("title"));
    }

    @Test
    public void testJSONOrgSample2() throws LazyException{
        String str="{\"menu\": {\n  \"id\": \"file\",\n  \"value\": \"File\",\n  \"popup\": {\n    \"menuitem\": [\n      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n    ]\n  }\n}}";
        LazyObject obj=new LazyObject(str);
        LazyObject m=obj.getJSONObject("menu");
        assertNotNull(m);
        assertEquals("file",m.getString("id"));
        m=m.getJSONObject("popup");
        assertNotNull(m);
        LazyArray a=m.getJSONArray("menuitem");
        assertNotNull(a);
        LazyObject o=a.getJSONObject(1);
        assertNotNull(o);
        assertEquals("Open",o.getString("value"));
    }

    @Test
    public void testComplexObject() throws Exception{
        String str="{"+
            "\"Type\":\"rating\","+
            "\"IsDisabled\":false,"+
            "\"EventID\":\"deadbeef-dead-beef-dead-beef00000001\","+
            "\"Record\":{"+
                "\"Item\":{"+
                    "\"ID\":2983980,"+
                    "\"Rating\":5,"+
                    "\"Type\":null,"+
                "},"+
                "\"User\":{"+
                    "\"ID\":478830012,"+
                    "\"First\":\"Ben\","+
                    "\"Last\":\"Boolean\","+
                    "\"Email\":\"foo@test.local\","+
                    "\"Title\":\"Chief Blame Officer\","+
                    "\"Company\":\"DoubleDutch\","+
                    "\"Department\":null"+
                "}"+
            "}"+
        "}";

        LazyObject obj=new LazyObject(str);
        LazyObject record=obj.getJSONObject("Record");
        assertNotNull(record);
        LazyObject item=record.getJSONObject("Item");
        assertEquals(item.getInt("ID"),2983980);
        assertEquals("rating",obj.getString("Type"));
        LazyObject user=record.getJSONObject("User");
        assertNotNull(user);
        assertEquals("Ben",user.getString("First"));
        assertEquals("DoubleDutch",user.getString("Company"));
        assertTrue(user.isNull("Department"));
    }
}