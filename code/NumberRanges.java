/**
* @author Joseph Mbabu
* NumberRanges.java
*/

import java.io.*;
import java.util.Scanner;

public class NumberRanges{
   private static class Node{
       private Node next;
       private Node previous;
       private int value;

       Node(int value){
           this.value = value;
       }

       // set next node
       public void set_next(Node next){
            this.next = next;
       }

       // get next node
       public Node get_next(){
            return this.next;
       }

      // set previous node
       public void  set_previous(Node previous){
             this.previous = previous;
       }

       // get previous node
       public Node get_previous(){
             return this.previous;
       }

       // set value
       public void set_value( int value){
            this.value = value;
       }

       // get value
       public int get_value(){
          return this.value;
       }
    }


    static Node head; // first node
    static Node last; // last node


    public NumberRanges(){
         head = null;
         last = null;
    }

    // add node
    public static void add(int value){
       Node current = head;
       Node new_node = new Node(value);

       if(head == null){
          head = new_node;
          last = new_node;
          return;
       }

       // if new node equals current node
       else if(new_node.get_value() == current.get_value() ){
              return;
       }

       // if new node is greater than current node
       else if(new_node.get_value() < current.get_value()){
              head = new_node;
              current.set_previous(new_node);
              new_node.set_next(current);
              return;
       }
       else{
            while(current.next !=  null){
                   if(new_node.get_value() > current.next.get_value()){
                       current = current.get_next();
                     }
                   // skip duplicates
                   else if(new_node.get_value() == current.get_next().get_value() ){
                          return;
                   }
                   else{
                       new_node.set_next(current.next);
                       current.next = new_node;
                       new_node.next.set_previous(new_node);
                       new_node.set_previous(current);
                       return;
                       }
            }
       }

       if(last.get_value() != new_node.get_value()){
          last.next = new_node;
          new_node.previous = last;
          last = new_node;
       }
    }

    // find line with letters
    public static boolean bad_input(String line){
          boolean flag_value = false;
          char [] ch = line.toCharArray();
          for(int i = 0;i < ch.length; i++ ){
              if(Character.isLetter(ch[i])){
                 flag_value = true;
                 break;
              }
          }
          return flag_value;
    }

    // check use of dash character
    public static boolean lone_dash(String line){
       boolean flag_value = false;
       for(int i = 0; i< line.length(); i++){
           for(int j = 1; j<line.length()-i; j++){
               String substr = line.substring(i,i+j);
               if(substr.equals(" - ") || substr.equals("- ")){
                  flag_value = true;
                  break;
               }
          }
       }
       return flag_value;
    }

    // validate number of values in data input
    public static boolean num_of_values(String line){
        boolean flag = false;
        String [] data = line.split("\\s+");
        if(data.length >= 1 && data.length <= 2)
           return flag = true;
        return flag;
    }

    // parses file for input data
    public static void read_file(String txt_file){
          String data_input = null;
          try{
              File file = new File(txt_file);
              Scanner scanner = new Scanner(file);

              System.out.println("Input");

              // skip over comments
              while( scanner.hasNextLine()){
                    data_input = scanner.nextLine();
                    data_input = data_input.trim();

                    if(! data_input.startsWith("//") && !(data_input.equals(""))){
                       if((bad_input(data_input) == true || lone_dash(data_input) == true ) && num_of_values(data_input) == true)
                           // bad input error message
                           System.out.printf("Bad input: %s.\n",data_input);

                       if(bad_input(data_input) == false && lone_dash(data_input) == false && !data_input.endsWith("-") && num_of_values(data_input) == true){
                           String [] values = data_input.split("\\s+");

                           // line with one value
                           if(values.length == 1){
                                add(Integer.parseInt(values[0]));
                           }

                           // line with two values and sort
                           else{
                                int val0 = Integer.parseInt(values[0]);
                                int val1 = Integer.parseInt(values[1]);

                                if(val0 > val1){ // range not in the right order
                                    int temp = val0;
                                    val0 = val1;
                                    val1 = temp;

                                    for(int i = val0; i <= val1; i++){
                                        add(i);
                                     }
                                }
                                else if(val0 < val1){ // range in the right order
                                    for(int i = val0; i <= val1; i++){
                                        add(i);
                                        //System.out.println(i);
                                     }
                                }
                                else // range with equal values
                                   add(val1);
                          }
                          System.out.println(data_input); // print output
                      }
                   }
               }
               System.out.println("\nOutput"); // output header
           }

          catch(FileNotFoundException e){
               System.out.println("File does not exist");
               System.exit(0);
          }
     }

    // print number range
    public static void print_numberRange(){
       Node current = head;
       System.out.print(current.get_value());

       while(current.get_next() != null){
            if(current.get_value() == (current.get_next()).get_value()-1 ){
               while(current.get_next() != null && current.get_value() == current.get_next().get_value()-1){
                    current = current.get_next();
               }
               System.out.print("-" + current.get_value());
            }
            else{
                System.out.print(", "+ current.get_next().get_value());
                current = current.get_next();
               }
       }
       System.out.println();
    }

    // main
    public static void main(String [] args){
       if(args.length == 0)
	        System.out.println("Error.Please enter the name of the input file.\n>");
       else
           read_file(args[0]);
           print_numberRange();
    }
}
