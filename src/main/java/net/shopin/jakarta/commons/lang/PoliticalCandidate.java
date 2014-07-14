package net.shopin.jakarta.commons.lang;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.*;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

