package ceng.ceng351.cengvacdb;
import ceng.ceng351.cengvacdb.ICENGVACDB;
import com.mysql.cj.Query;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class CENGVACDB implements ICENGVACDB {
    private static String user = ""; // TODO: Your userName
    private static String password = ""; //  TODO: Your password
    private static String host = ""; // host name
    private static String database = ""; // TODO: Your database name
    private static int port = 8080; // port

    private static Connection connection = null;

    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=FALSE";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection =  DriverManager.getConnection(url, user, password);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createTables() {
        int count = 0;
        Statement st = null;
        //USER TABLE
        try {
            st = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS User (\n" +
                "    userID int,\n" +
                "    userName varchar(30),\n" +
                "    age int,\n" +
                "    address varchar(150),\n" +
                "    password varchar(30),\n" +
                "    status varchar(15),\n" +
                "    PRIMARY KEY (userID)\n" +
                ");";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //VACCINE TABLE
        try {
            st = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS Vaccine (\n" +
                    "    code int,\n" +
                    "    vaccinename varchar(30),\n" +
                    "    type varchar(30),\n" +
                    "    PRIMARY KEY (code)\n" +
                    ");";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Vaccination Table
        try {
            st = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS Vaccination (\n" +
                    "    code int,\n" +
                    "    userID int,\n" +
                    "    dose int,\n" +
                    "    vacdate date,\n" +
                    "    PRIMARY KEY (code,userID,dose),\n" +
                    "    FOREIGN KEY (code) REFERENCES Vaccine(code),\n" +
                    "    FOREIGN KEY (userID) REFERENCES User(userID)\n" +
                    ");";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //AllergicSideEffect Table
        try {
            st = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS AllergicSideEffect (\n" +
                    "    effectcode int,\n" +
                    "    effectname varchar(50),\n" +
                    "    PRIMARY KEY (effectcode)\n" +
                    ");";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        //Seen Table
        try {
            st = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS Seen (\n" +
                    "    effectcode int,\n" +
                    "    code int,\n" +
                    "    userID int,\n" +
                    "    date date,\n" +
                    "    degree varchar(30),\n" +
                    "    PRIMARY KEY (effectcode,code,userID),\n" +
                    "    FOREIGN KEY (effectcode) REFERENCES AllergicSideEffect(effectcode),\n" +
                    "    FOREIGN KEY (code) REFERENCES Vaccine(code)\n" +
                    "    ON DELETE CASCADE,\n" +
                    "    FOREIGN KEY (userID) REFERENCES User(userID)\n" +
                    ");";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return count;
    }

    @Override
    public int dropTables() {
        int count = 0;
        Statement st = null;
        //USER TABLE
        try {
            st = connection.createStatement();
            String query = "DROP TABLE Seen ;\n";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Vaccine TABLE
        try {
            st = connection.createStatement();
            String query = "DROP TABLE Vaccination ;\n";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Vaccination TABLE
        try {
            st = connection.createStatement();
            String query = "DROP TABLE User ;\n";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //AllergicSideEffect TABLE
        try {
            st = connection.createStatement();
            String query = "DROP TABLE Vaccine ;\n";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Seen TABLE
        try {
            st = connection.createStatement();
            String query = "DROP TABLE AllergicSideEffect ;\n";

            boolean result = st.execute(query);
            count++;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return count;
    }

    @Override
    public int insertUser(User[] users) {
        Statement st = null;
        int len = users.length;
        int rowAdded=0;
        for(int i=0;i<len;i++){
            try {
                st = connection.createStatement();
                String query = "INSERT INTO User VALUES('"+
                        String.valueOf(users[i].getUserID())+"','"+
                        users[i].getUserName()+"','"+
                        String.valueOf(users[i].getAge())+"','"+
                        users[i].getAddress()+"','"+
                        users[i].getPassword()+"','"+
                        users[i].getStatus()+"'"+
                        ");\n";
                boolean result = st.execute(query);
                rowAdded++;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowAdded;
    }

    @Override
    public int insertAllergicSideEffect(AllergicSideEffect[] sideEffects) {
        Statement st = null;
        int len = sideEffects.length;
        int rowAdded=0;
        for(int i=0;i<len;i++){
            try {
                st = connection.createStatement();
                String query = "INSERT INTO AllergicSideEffect VALUES('"+
                        String.valueOf(sideEffects[i].getEffectCode())+"','"+
                        sideEffects[i].getEffectName()+
                        "');\n";
                boolean result = st.execute(query);
                rowAdded++;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowAdded;
    }

    @Override
    public int insertVaccine(Vaccine[] vaccines) {
        Statement st = null;
        int len = vaccines.length;
        int rowAdded=0;
        for(int i=0;i<len;i++){
            try {
                st = connection.createStatement();
                String query = "INSERT INTO Vaccine VALUES('"+
                        String.valueOf(vaccines[i].getCode())+"','"+
                        vaccines[i].getVaccineName()+"','"+
                        vaccines[i].getType()+
                        "');\n";
                boolean result = st.execute(query);
                rowAdded++;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowAdded;
    }

    @Override
    public int insertVaccination(Vaccination[] vaccinations) {
        Statement st = null;
        int len = vaccinations.length;
        int rowAdded=0;
        for(int i=0;i<len;i++){
            try {
                st = connection.createStatement();
                String query = "INSERT INTO Vaccination VALUES('"+
                        String.valueOf(vaccinations[i].getCode())+"','"+
                        String.valueOf(vaccinations[i].getUserID())+"','"+
                        String.valueOf(vaccinations[i].getDose())+"','"+
                        vaccinations[i].getVacdate()+
                        "');\n";
                boolean result = st.execute(query);
                rowAdded++;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowAdded;

    }

    @Override
    public int insertSeen(Seen[] seens) {
        Statement st = null;
        int len = seens.length;
        int rowAdded=0;
        for(int i=0;i<len;i++){
            try {
                st = connection.createStatement();
                String query = "INSERT INTO Seen VALUES('"+
                        String.valueOf(seens[i].getEffectcode())+"','"+
                        String.valueOf(seens[i].getCode())+"','"+
                        seens[i].getUserID()+"','"+
                        seens[i].getDate()+"','"+
                        seens[i].getDegree()+
                        "');\n";
                boolean result = st.execute(query);
                rowAdded++;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return rowAdded;

    }

    @Override
    //select C.code from Vaccine C where C.code not in (select distinct V.code from Vaccination V,Vaccine F where V.cod
    //e=F.code);
    public Vaccine[] getVaccinesNotAppliedAnyUser() {
        Statement st = null;
        ArrayList<Vaccine> vaccines = new ArrayList<Vaccine>();
        try {
            st = connection.createStatement();
            String query = "select distinct * from Vaccine C"+
                    " where C.code not in "+
                    " (select distinct V.code"+
                    " from Vaccination V,Vaccine F"+
                    " where V.code=F.code);";
            ResultSet result = st.executeQuery(query);
            while(result.next()){
                int code = result.getInt("code");
                String vName = result.getString("vaccinename");
                String type = result.getString("type");
                Vaccine v = new Vaccine(code ,vName, type);
                vaccines.add(v);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Vaccine result[] = new Vaccine[vaccines.size()];

        for(int i=0;i<vaccines.size();i++){
            Vaccine temp = vaccines.get(i);
            result[i]= temp;
            }
        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getVaccinatedUsersforTwoDosesByDate(String vacdate) {
        Statement st = null;
        ArrayList<QueryResult.UserIDuserNameAddressResult> users = new ArrayList<QueryResult.UserIDuserNameAddressResult>();
        try {
            st = connection.createStatement();
            String query = "select distinct userID,userName,Address from User S where S.userID in("+
                    " select V1.userID from Vaccination V1 where "+
                    " V1.vacdate>'"+vacdate+"' and V1.userID not in"+
                    " (select V.userID from Vaccination V where V.vacdate>'"+vacdate+"'"+
                    " group by V.userID having count(*)>2) "+
                    " group by userID having count(*)>1 );";
            ResultSet result = st.executeQuery(query);
            while(result.next()){
                String userID = result.getString("userID");
                String name = result.getString("userName");
                String address = result.getString("address");
                QueryResult.UserIDuserNameAddressResult v = new QueryResult.UserIDuserNameAddressResult(userID ,name, address);
                users.add(v);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        QueryResult.UserIDuserNameAddressResult result[] = new QueryResult.UserIDuserNameAddressResult[users.size()];

        for(int i=0;i<users.size();i++){
            QueryResult.UserIDuserNameAddressResult temp = users.get(i);
            result[i]= temp;
        }
        return result;
    }

    @Override
    public Vaccine[] getTwoRecentVaccinesDoNotContainVac() {
        Statement st = null;
        ArrayList<Vaccine> vaccines = new ArrayList<Vaccine>();
        String[] arr = {"a","c"};
        try {
            st = connection.createStatement();
            String query = " select * from Vaccination V,Vaccine S where V.code=S.code order by V.vacdate desc;";
            ResultSet result = st.executeQuery(query);
            int counter = 0;
            while(result.next() && counter<2){
                int code = result.getInt("code");
                String type = result.getString("type");
                String name = result.getString("vaccinename");
                if(!(name.toLowerCase().contains("vac"))){
                    if(counter==0){
                        arr[0]=name;
                        Vaccine v = new Vaccine(code ,name, type);
                        vaccines.add(v);
                        counter++;
                    }
                    else if(counter==1){
                        if(arr[0].equals(name)){
                            continue;
                        }
                        else{
                            arr[1]=name;
                            Vaccine v = new Vaccine(code ,name, type);
                            vaccines.add(v);
                            counter++;
                        }
                    }

                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Vaccine result[] = new Vaccine[vaccines.size()];

        for(int i=0;i<vaccines.size();i++){
            Vaccine temp = vaccines.get(i);
            result[i]= temp;
        }
        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getUsersAtHasLeastTwoDoseAtMostOneSideEffect() {
        Statement st = null;
        ArrayList<QueryResult.UserIDuserNameAddressResult> users = new ArrayList<QueryResult.UserIDuserNameAddressResult>();
        try {
            st = connection.createStatement();
            String query = "create temporary table e(userID int);";
            st.execute(query);
            String query3= " insert into e (((select userID from Seen group by userID having count(*)<2)"+
                    " union (select distinct User.userID from User where User.userID not in(select userID from Seen))));";
            st.execute(query3);
            String query2 =" select userID,userName,address from User where userID in"+
                    " (select distinct userID from e where userID in "+
                    "(select V.userID from Vaccination V group by V.userID having count(*)>1) order by userID) order by userID;";
            ResultSet result = st.executeQuery(query2);
            while(result.next()){
                String userID = result.getString("userID");
                String name = result.getString("userName");
                String address = result.getString("address");
                QueryResult.UserIDuserNameAddressResult v = new QueryResult.UserIDuserNameAddressResult(userID ,name, address);
                users.add(v);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        QueryResult.UserIDuserNameAddressResult result[] = new QueryResult.UserIDuserNameAddressResult[users.size()];

        for(int i=0;i<users.size();i++){
            QueryResult.UserIDuserNameAddressResult temp = users.get(i);
            result[i]= temp;
        }
        return result;

    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getVaccinatedUsersWithAllVaccinesCanCauseGivenSideEffect(String effectname) {
        Statement st = null;
        ArrayList<QueryResult.UserIDuserNameAddressResult> users = new ArrayList<QueryResult.UserIDuserNameAddressResult>();
        try {
            st = connection.createStatement();
            String query = "select userID,userName,address from User where userID in"+
                    " (select distinct userID from Vaccination V where not exists"+
                    " (select userID from User S where V.code not in "+
                    "(select distinct V.code from Vaccine V,Seen S,AllergicSideEffect A "+
                    "where V.code = S.code and S.effectcode=A.effectcode and A.effectname='loss_of_speech'))) order by userID;";

            ResultSet result = st.executeQuery(query);
            while(result.next()){
                String userID = result.getString("userID");
                String name = result.getString("userName");
                String address = result.getString("address");
                QueryResult.UserIDuserNameAddressResult v = new QueryResult.UserIDuserNameAddressResult(userID ,name, address);
                users.add(v);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        QueryResult.UserIDuserNameAddressResult result[] = new QueryResult.UserIDuserNameAddressResult[users.size()];

        for(int i=0;i<users.size();i++){
            QueryResult.UserIDuserNameAddressResult temp = users.get(i);
            result[i]= temp;
        }
        return result;
    }

    @Override
    public QueryResult.UserIDuserNameAddressResult[] getUsersWithAtLeastTwoDifferentVaccineTypeByGivenInterval(String startdate, String enddate) {
        //select userID, userName, address from User S where userID in (select userID from Vaccination where '2021-04-01'<vacdate and vacdate<'2021-11-01' and userID in (select distinct V1.userID from Vaccination V1,Vaccination V2 where V1.userID=V2.userID and V1.code<>V2.code) group by userID having count(*)>1);
        Statement st = null;
        ArrayList<QueryResult.UserIDuserNameAddressResult> users = new ArrayList<QueryResult.UserIDuserNameAddressResult>();
        try {
            st = connection.createStatement();
            String query = "select userID, userName, address from User S where userID in "+
                    "(select userID from Vaccination where '"+startdate+"'<=vacdate and vacdate<='"+enddate+"' and userID in"+
                    " (select distinct V1.userID from Vaccination V1,Vaccination V2 where V1.userID=V2.userID and V1.code<>V2.code)"+
                    " group by userID having count(*)>1) order by userID;";

            ResultSet result = st.executeQuery(query);
            while(result.next()){
                String userID = result.getString("userID");
                String name = result.getString("userName");
                String address = result.getString("address");
                QueryResult.UserIDuserNameAddressResult v = new QueryResult.UserIDuserNameAddressResult(userID ,name, address);
                users.add(v);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        QueryResult.UserIDuserNameAddressResult result[] = new QueryResult.UserIDuserNameAddressResult[users.size()];

        for(int i=0;i<users.size();i++){
            QueryResult.UserIDuserNameAddressResult temp = users.get(i);
            result[i]= temp;
        }
        return result;
    }

    @Override
    public AllergicSideEffect[] getSideEffectsOfUserWhoHaveTwoDosesInLessThanTwentyDays() {
        return new AllergicSideEffect[0];
    }

    @Override
    public double averageNumberofDosesofVaccinatedUserOverSixtyFiveYearsOld() {
        //select V.userID,count(*) from Vaccination V,User S where V.userID = S.userID and S.age>65 group by userID;
        Statement st = null;
        double total = 0, len = 0;
        try {
            st = connection.createStatement();
            String query = "select V.userID,count(*) as counter from Vaccination V,User S where V.userID = S.userID and S.age>65 group by userID;";

            ResultSet result = st.executeQuery(query);
            while(result.next()){
                Integer count = result.getInt("counter");
                total+=count;
                len++;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return total/len;
    }

    @Override
    public int updateStatusToEligible(String givendate) {
        //select userID,MAX(vacdate) from Vaccination  group by userID ;;
        Statement st = null;
        SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
        long daydiff=0;
        int rowCount=0;
        try {
            st = connection.createStatement();
            String query = "select userID,MAX(vacdate) as last from Vaccination  group by userID ;";

            ResultSet result = st.executeQuery(query);
            while(result.next()){
                String date = result.getString("last");
                int userID = result.getInt("userID");
                try {
                    Date currentTime = obj.parse(givendate);
                    Date lastVaccine = obj.parse(date);
                    long timediff = currentTime.getTime()-lastVaccine.getTime();
                    daydiff = (timediff / (1000*60*60*24)) % 365;
                }catch(ParseException e){
                    e.printStackTrace();
                }
                if(daydiff>120){
                    try {
                        st = connection.createStatement();
                        String query2 = "update User set status='Eligible' where userID="+userID+" and status<>'Eligible';";
                        int r = st.executeUpdate(query2);
                        rowCount+=r;
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return rowCount;
    }

    @Override
    public Vaccine deleteVaccine(String vaccineName) {
        Statement st = null;
        Vaccine v= new Vaccine(0,"","");
        try {
            st = connection.createStatement();
            String query = "select * from Vaccine where vaccinename='"+vaccineName+"';";

            ResultSet result = st.executeQuery(query);
            while(result.next()){
                Integer code = result.getInt("code");
                String name = result.getString("vaccinename");
                String type = result.getString("type");
                v.setCode(code);
                v.setVaccineName(name);
                v.setType(type);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return v;
    }
}