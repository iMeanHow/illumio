import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Firewall {
    //xxx1 rep tcp inbound
    //xxx2 rep tcp outbound
    //xxx3 rep udp inbound
    //xxx4 rep udp outbound
    //map port with ip address here
    //since ipv4 have 4294967295 combinations which is too many for an array,
    // use HashMap to map port and list of string value here

    Map<Integer, List<String>> map1;
    Map<Integer, List<String>> map2;
    Map<Integer, List<String>> map3;
    Map<Integer, List<String>> map4;

    public Firewall(String filename){
        map1=new HashMap<>();   //tcp inbound
        map2=new HashMap<>();   //tcp outbound
        map3=new HashMap<>();   //udp inbound
        map4=new HashMap<>();   //udp outbound
        try {
            read_file(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void read_file(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner sc = new Scanner(file);
        while(sc.hasNext()){
            String []condition=sc.nextLine().split(",");
            switch (condition[0]){

                case "inbound":
                    if(condition[1].equals("tcp"))  {
                        insert_rule(1,condition[2],condition[3]);
                    }
                    else if(condition[1].equals("udp"))   {
                        insert_rule(3,condition[2],condition[3]);
                    }
                    break;

                case "outbound":
                    if(condition[1].equals("tcp")){
                        insert_rule(2,condition[2],condition[3]);
                    }
                    else if(condition[1].equals("udp"))  {
                        insert_rule(4,condition[2],condition[3]);
                    }
                    break;

                default:
                    System.out.println("Error direction input!");
            }

        }
    }



    public void insert_rule(int type,String port, String ip_address){
        if(port.contains("-")){
            String ports[]=port.split("-");
            int l=Integer.valueOf(ports[0]),h=Integer.valueOf(ports[1]),i;
            for(i=l;i<=h;i++){
                insert_port_ip(type,i,ip_address);
            }

        }
        else{
            insert_port_ip(type, Integer.valueOf(port),ip_address);
        }
    }

    public void insert_port_ip(int type, int port, String ip_address){
        switch (type){
            case 1:
                if(map1.containsKey(port)){
                    List<String> tmp=map1.get(port);
                    tmp.add(ip_address);
                    map1.put(port,tmp);
                }
                else{
                    List<String> tmp=new ArrayList<>();
                    tmp.add(ip_address);
                    map1.put(port,tmp);
                }
                break;

            case 2:
                if(map2.containsKey(port)){
                    List<String> tmp=map2.get(port);
                    tmp.add(ip_address);
                    map2.put(port,tmp);
                }
                else{
                    List<String> tmp=new ArrayList<>();
                    tmp.add(ip_address);
                    map2.put(port,tmp);
                }
                break;

            case 3:
                if(map3.containsKey(port)){
                    List<String> tmp=map3.get(port);
                    tmp.add(ip_address);
                    map3.put(port,tmp);
                }
                else{
                    List<String> tmp=new ArrayList<>();
                    tmp.add(ip_address);
                    map3.put(port,tmp);
                }
                break;

            case 4:
                if(map4.containsKey(port)){
                    List<String> tmp=map4.get(port);
                    tmp.add(ip_address);
                    map4.put(port,tmp);
                }
                else{
                    List<String> tmp=new ArrayList<>();
                    tmp.add(ip_address);
                    map4.put(port,tmp);
                }
                break;

            default:
                break;



        }
    }

    public boolean accept_packet(String direction, String protocol, int port, String ip_address){
        switch (direction){

            case "inbound":
                if(protocol.equals("tcp")){
                    if(map1.containsKey(port)){
                        List<String> addresses=map1.get(port);
                        for(int i=0;i<addresses.size();i++){
                            String valid_address=addresses.get(i);
                            if(ip_matcher(valid_address,ip_address))
                                return true;
                        }

                    }
                }
                else if(protocol.equals("udp")) {
                    if(map3.containsKey(port)){
                        List<String> addresses=map3.get(port);
                        for(int i=0;i<addresses.size();i++){
                            String valid_address=addresses.get(i);
                            if(ip_matcher(valid_address,ip_address))
                                return true;
                        }
                    }
                }
                else {
                    System.out.println("Error protocol input!");
                }
                break;

            case "outbound":
                if(protocol.equals("tcp")){
                    if(map2.containsKey(port)){
                        List<String> addresses=map2.get(port);
                        for(int i=0;i<addresses.size();i++){
                            String valid_address=addresses.get(i);
                            if(ip_matcher(valid_address,ip_address))
                                return true;
                        }
                    }
                    else return false;
                }
                else if(protocol.equals("udp")) {
                    if(map4.containsKey(port)){
                        List<String> addresses=map4.get(port);
                        for(int i=0;i<addresses.size();i++){
                            String valid_address=addresses.get(i);
                            if(ip_matcher(valid_address,ip_address))
                                return true;
                        }
                    }
                    else return false;
                }
                else {
                    System.out.println("Error protocol input!");
                }
                break;

            default:
                System.out.println("Error direction input!");
        }
        return false;
    }

    private boolean ip_matcher(String valid_address,String ip_address){
        if(valid_address.contains("-")){
//            if the ip stored is a range, change it into a long value then compare with input one
            String []addresses=valid_address.split("-");
            String[]ip_min = addresses[0].split("\\.");
            String[]ip_max = addresses[1].split("\\.");
            String[] cur_ip=ip_address.split("\\.");
            long lower_bound=(Long.parseLong(ip_min[0]) << 24) + (Long.parseLong(ip_min[1]) << 16) + (Long.parseLong(ip_min[2]) << 8) + Long.parseLong(ip_min[3]);
            long upper_bound=(Long.parseLong(ip_max[0]) << 24) + (Long.parseLong(ip_max[1]) << 16) + (Long.parseLong(ip_max[2]) << 8) + Long.parseLong(ip_max[3]);
            long ip=(Long.parseLong(cur_ip[0]) << 24) + (Long.parseLong(cur_ip[1]) << 16) + (Long.parseLong(cur_ip[2]) << 8) + Long.parseLong(cur_ip[3]);
            if(ip >= lower_bound && ip <= upper_bound)
                return true;
        }
        else{
            return valid_address.equals(ip_address);
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Firewall f = new Firewall("rules_manualTestUsed.csv");
        System.out.println(f.accept_packet("inbound", "tcp", 1, "1.1.1.1"));
        System.out.println(f.accept_packet("inbound", "tcp", 9, "255.255.255.254"));
        System.out.println(f.accept_packet("inbound", "tcp", 88, "192.168.1.2"));
        System.out.println(f.accept_packet("inbound", "udp", 2200, "192.168.2.1"));
        System.out.println(f.accept_packet("outbound", "tcp", 2811, "192.168.10.11"));
        System.out.println(f.accept_packet("outbound", "udp", 65535, "7.7.7.7"));
        System.out.println(f.accept_packet("outbound", "udp", 65535, "1.1.1.1"));
        System.out.println(f.accept_packet("outbound", "udp", 1, "255.255.255.255"));
        System.out.println(f.accept_packet("inbound", "tcp", 789, "192.168.1.2"));
        System.out.println(f.accept_packet("outbound", "udp", 4396, "52.12.48.92"));
    }
}
