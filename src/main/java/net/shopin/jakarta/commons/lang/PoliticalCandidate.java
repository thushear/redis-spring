package net.shopin.jakarta.commons.lang;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.*;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.lang.time.StopWatch;

import java.math.BigDecimal;
import java.util.*;

/**
 * 说明: commons-lang包 学习
 * User: kongming
 * Date: 14-7-14
 * Time: 下午2:31
 */
public class PoliticalCandidate {

    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private BigDecimal moneyRaised;
    private State homeState;


    public static final DoubleRange LAT_RANGE = new DoubleRange(-90.0,90.0);



    public PoliticalCandidate(String firstName, String lastName, Date dateOfBirth, BigDecimal moneyRaised, State homeState) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.moneyRaised = moneyRaised;
        this.homeState = homeState;
    }





    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public BigDecimal getMoneyRaised() {
        return moneyRaised;
    }

    public void setMoneyRaised(BigDecimal moneyRaised) {
        this.moneyRaised = moneyRaised;
    }

    public State getHomeState() {
        return homeState;
    }

    public void setHomeState(State homeState) {
        this.homeState = homeState;
    }

    @Override
    public String toString() {
        //use reflection to automate toString
        //return ReflectionToStringBuilder.toString(this);
        //customize toString
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("lastName",lastName)
                .append("firstName",firstName)
                .toString();
    }


    @Override
    public boolean equals(Object o) {
        boolean equals = false;
        if(o!=null && PoliticalCandidate.class.isAssignableFrom(o.getClass())){
            PoliticalCandidate pc = (PoliticalCandidate) o;
            equals = (new EqualsBuilder().append(firstName,pc.firstName)
            .append(lastName,pc.lastName)).isEquals();

        }
        return equals;
    }

    //a hashcode which creates a hash from the two unique identifiers
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17,37)
                .append(firstName)
                .append(lastName).toHashCode();

    }


    /**
     * use commons-lang    CompareToBuilder
     * @param o
     * @return
     */
    public int compareTo(Object o ){
       int compare = -1;
        if(o!=null && PoliticalCandidate.class.isAssignableFrom(o.getClass())){
            PoliticalCandidate pc = (PoliticalCandidate) o;
            compare = (new CompareToBuilder().append(firstName,pc.firstName)
            .append(lastName,pc.lastName)).toComparison();
        }
        return compare;
    }


    /**
     * 轮子 不要自己造轮子
     * @param args
     */
    public static void main(String[] args) {
        State state = new State("va","Virgina");

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR,1743);
        calendar.set(Calendar.MONTH,Calendar.APRIL);
        calendar.set(Calendar.DAY_OF_MONTH,13);
        Date dob = calendar.getTime();
        BigDecimal money = new BigDecimal(29222.33);
        PoliticalCandidate candidate = new PoliticalCandidate("Jefferson","Thomas",dob,money,state);
        System.out.println(candidate);
        System.out.println(candidate.hashCode());
        PoliticalCandidate candidate1 = new PoliticalCandidate("Jeffersons","Thomas",dob,money,state);
        System.out.println(candidate.compareTo(candidate1));


        //---------------------------printing an array--------------
        int[] intArray = new int[]{2,3,4,5,6};
        System.out.println("intArray:" + intArray);
        System.out.println("intArray:" + ArrayUtils.toString(intArray));

        String[] strings = new String[]{"blue","green",null,"yellow"};
        System.out.println("Strings:" + ArrayUtils.toString(strings,"Unkown"));


        //-------------------------cloning and reversing arrays
        long[] array = {1,2,3,4,5,6};
        long[] reversed = ArrayUtils.clone(array);
        ArrayUtils.reverse(reversed);
        System.out.println("Original: " +  ArrayUtils.toString(array)) ;
        System.out.println("Reversed: " + ArrayUtils.toString(reversed));

        //transform between objects arrays and primitie arrays

        long[] primitiveArray = new long[]{12,100,133,2333};
        Long[] objectArray = ArrayUtils.toObject(primitiveArray);
        System.out.println("object array:" +  objectArray);

        //finding items in an array
        String[] stringArray = {"Red","Blue","Brown","Red","Orange"};
        boolean containsBlue = ArrayUtils.contains(stringArray,"Blue");
        System.out.println(containsBlue);
        int indexOfRed = ArrayUtils.indexOf(stringArray,"Red");
        System.out.println(indexOfRed);
        int lastIndexOfRed = ArrayUtils.lastIndexOf(stringArray,"Red");
        System.out.println(lastIndexOfRed);

        //多维数组构造Map
        Object[] weightArray = new Object[][]{
                {"H",new Double(1.007)},
                {"He",new Double(4.002)}

        };
        //create a map
        Map weights = ArrayUtils.toMap(weightArray);
        Double h = (Double) weights.get("H");
        System.out.println("h= " +  h);

        //格式化时间
        Date now = new Date();
        String isoDT = DateFormatUtils.ISO_DATETIME_FORMAT.format(now);
        System.out.println("It is currently:" + isoDT);
        //自定义格式
        //FastDateFormat formatter = new FastDateFormat("yyyy-mm-dd hh24:mi:ss", TimeZone.getDefault(),Locale.getDefault());
        FastDateFormat  fastDateFormat = FastDateFormat.getInstance("yyyy-mm-dd");
        String output = fastDateFormat.format(new Date());
        System.out.println(output);

        //时间工具类
        FastDateFormat dtFormat = DateFormatUtils.ISO_DATETIME_FORMAT;
        Date current = new Date();
        Date nearestHour = DateUtils.round(current,Calendar.HOUR);
        Date nearetDay = DateUtils.round(current,Calendar.DAY_OF_MONTH);
        Date nearestYear = DateUtils.round(current,Calendar.YEAR);
        System.out.println("Now:" + dtFormat.format(current));
        System.out.println("nearest hour :" + dtFormat.format(nearestHour));
        System.out.println("nearest day:" + dtFormat.format(nearetDay));
        System.out.println("nearest year:" + dtFormat.format(nearestYear));

        //id 生成器


        //入参验证
        //doSomeThing(-1,null,null);
        //doSomeThing(1,new Object[]{},null);

        List list =  Collections.synchronizedList(new ArrayList());
        list.add(null);
        //doSomeThing(1, new Object[]{1}, list);
       // doSomeThing2(-1000d);

        //计时
        StopWatch clock = new StopWatch();
        System.out.println("Time to Math.sin(0.34 ) ten million times");
        clock.start();
        for (int i=0;i<10000000;i++){
            Math.sin(0.34);
        }
        clock.stop();
        System.out.println("it takes " + clock.getTime() + " milliseconds");

        //split
        clock.reset();
        clock.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        clock.split();
        System.out.println("Split time after 1 sec :" + clock.getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        System.out.println("Split time after 2 sec:"  + clock.getTime());
        clock.unsplit();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        System.out.println("Time after 3 sec:" + clock.getTime());

    }

    public static void doSomeThing2(Double low){
        Validate.isTrue(LAT_RANGE.containsDouble(low));
    }


    /**
     * 入参验证
     * @param param1
     * @param param2
     * @param param3
     */
    public static void doSomeThing(int param1,Object[] param2,Collection param3){
        Validate.isTrue(param1>0,"param must be greater than zero");
        Validate.notEmpty(param2,"param2 must not be empty");
        Validate.noNullElements(param3);


    }



}
class State{
    private String alias;
    private String name;

    State(String alias, String name) {
        this.alias = alias;
        this.name = name;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

