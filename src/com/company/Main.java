///////////////   //             //    //
//                //                   //                                  /////////////
//                //             //    //                                  //         //
//                //             //    //                                  //         //
//////////////    ///////////    //    /////////////      ////////////     /////////////
//    //       //    //    //         //      //        //     //
//    //       //    //    //         //      //        //     //
//    //       //    //    //         //      ////////////     //
//////////////    //       //    //    //         //                  //   //

// Course:  OPERATING SYSTEMS - 2019
// Title:   Assignment III
// Program: CPU Schedulers Simulator
// Author:  SHIHAP AHMED MOHAMED(20170129)
// Date:    07 Dec 2019


package com.company;

import javax.naming.Name;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.lang.Math;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import java.io.FileWriter;

class MyComparator_arrival_time implements Comparator<process>
{
    public int compare (process x , process y)
    {
        return x.arrival_time - y.arrival_time ;
    }
}


class MyComparator_brust_time implements Comparator<process>
{
    public int compare (process x , process y)
    {
        return x.brust_time - y.brust_time ;
    }
}

class MyComparator_AG implements Comparator<process>
{
    public int compare (process x , process y)
    {
        return x.ag - y.ag ;
    }
}

class MyComparator_priority implements Comparator<process>
{
    public int compare (process x , process y)
    {
        return x.priority - y.priority ;
    }
}

public class Main {




    //JFrame -> the window that you add everything at it
    JFrame frame = new JFrame();
    Main(output o)
    {
        frame.setTitle("the process bar");
        frame.setSize(3000,2000);/*set the width , height for frame*/
        frame.setLayout(null);/*the method that you want to use to organize your elements automatic*/
        frame.setVisible(true);/*make frame visible or invisible*/


        for (int j = 0 ; j < o.code.size()/*loop at the code*/ ; j++)
        {
            //code label
            JLabel jLabel = new JLabel("|"/*text of label*/);
            jLabel.setBounds(10+(j*10) , 200 , 5000 , 100);
            //get_the_color
            Color color;
            String temp = "black" ;
            for (int i = 0 ; i < o.processes.size() ; i++)
            {
                if (o.code.get(j).equals(o.processes.get(i).name))
                {
                    temp = o.processes.get(i).color ;
                    break ;
                }
            }


            try {
                Field field = Class.forName("java.awt.Color").getField(temp);
                color = (Color)field.get(null);
            } catch (Exception e) {
                color = Color.BLACK; // Not defined
            }
            jLabel.setForeground(color);
            jLabel.setFont(new Font("Serif", Font.PLAIN, 100));


            frame.add(jLabel);/*adding the button to the frame*/
        }




        for (int i = 0 ; i < o.processes.size() /*size of process*/ ; i++)
        {

            //process label
            JLabel jLabel2 = new JLabel(o.processes.get(i).name+"->"+o.processes.get(i).color/*text of label*/);
            jLabel2.setBounds(10 , 300+(i*50) , 5000 , 100);
            //get_the_color
            //get_the_color
            Color color;

            try {
                Field field = Class.forName("java.awt.Color").getField(o.processes.get(i).color);
                color = (Color)field.get(null);
            } catch (Exception e) {
                color = Color.BLACK; // Not defined
            }
            jLabel2.setForeground(color);
            jLabel2.setFont(new Font("Serif", Font.PLAIN, 50));


            frame.add(jLabel2);/*adding the button to the frame*/

        }


    }



    public static output sjf()//done
    {
        Scanner sc = new Scanner (System.in) ;
        output o = new output() ;//1
        int n ;//number of processes
        System.out.print("enter number of processes you want to simulate :");
        n = sc.nextInt();

        //taking the processes
        ArrayList<process> arr = new ArrayList<process>();
        for (int i = 0 ; i < n ; i++)
        {
            process p = new process() ;
            System.out.print("enter peocess no." + (i+1) + " name: ");
            p.name = sc.next();
            System.out.print("enter peocess no." + (i+1) + " arrival time: ");
            p.arrival_time = sc.nextInt();
            System.out.print("enter peocess no." + (i+1) + " brust time: ");
            p.brust_time = sc.nextInt();
            p.priority = 0 ;

            System.out.print("enter peocess no." + (i+1) + " color: ");//2
            p.color = sc.next();

            arr.add(p);
            o.processes.add(p);//3
        }


        //sort the arr acording to brust time
        Collections.sort(arr,new Comparator<process>(){
            public int compare(process x,process y){
                return x.arrival_time - y.arrival_time ;
            }
        });



        PriorityQueue<process> pq = new PriorityQueue<process>(n,new MyComparator_brust_time());

        int    last_process_time = 0 ;
        double Average_Waiting_Time = 0 ;
        double Average_Turnaround_Time = 0 ;
        //execution of the processes
        while(!arr.isEmpty() || !pq.isEmpty())
        {

            //adding
            int num_of_deleting = 0 ;
            for (int i = 0 ; i < arr.size() ; i++)
            {
                if(arr.get(i).arrival_time <= last_process_time)
                {
                    pq.add(arr.get(i));//add the process at the pq
                    num_of_deleting++;
                }

            }

            for (int i = 0 ; i < num_of_deleting ; i++)
            {
                arr.remove(0);//delete the first element from arr
            }



            //executing

            process p = pq.peek();
            pq.poll();


            int start_time ;


            ///calculate waiting
            if (last_process_time>p.arrival_time)///there is waiting here
            {
                start_time = last_process_time ;
            }
            else///there is no waiting
            {
                start_time = p.arrival_time ;
            }

            int waiting_time = start_time - p.arrival_time ;

            int Turnaround_time = start_time + p.brust_time ;


            last_process_time = Turnaround_time ;

            Average_Waiting_Time += waiting_time;

            if (!arr.isEmpty() || !pq.isEmpty())
            {
                Average_Turnaround_Time += Turnaround_time;
            }


            System.out.println("name : " + p.name);
            System.out.println("brust_time : " + p.brust_time);
            System.out.println("arrival_time : " + p.arrival_time);
            System.out.println("start_time : " + start_time);
            System.out.println("waiting_time : " + waiting_time);
            System.out.println("Turnaround_time : " + Turnaround_time);
            System.out.println("\n\n");
            //System.out.println("Average_Waiting_Time : " + Average_Waiting_Time);
            //System.out.println("Average_Turnaround_Time : " + Average_Turnaround_Time);
/*
        System.out.println("printing the array\n");
        for (int i = 0 ; i < arr.size() ; i++)
        {
            System.out.println("name : " + arr.get(i).name);
            System.out.println("brust_time : " + arr.get(i).brust_time);
        }
        System.out.println("\n\n");
*/



         for (int i = 0 ; i < p.brust_time ; i++)
         {
             o.code.add(p.name);//4
         }

        }
        System.out.println("final_Average_Waiting_Time = " + (Average_Waiting_Time/n));
        System.out.println("final_Average_Turnaround_Time = " + (Average_Turnaround_Time/n));

        return o ;

    }//sjf

    public static output srtf()//done
    {
        Scanner sc = new Scanner (System.in) ;
        output o = new output() ;//1
        int n ;//number of processes
        int c ;//context switching
        System.out.print("enter number of processes you want to simulate :");
        n = sc.nextInt();
        System.out.print("enter number of context switching :");
        c = sc.nextInt();

        //taking the processes
        ArrayList<process> arr = new ArrayList<process>();
        for (int i = 0 ; i < n ; i++)
        {
            process p = new process() ;
            System.out.print("enter peocess no." + (i+1) + " name: ");
            p.name = sc.next();
            System.out.print("enter peocess no." + (i+1) + " arrival time: ");
            p.arrival_time = sc.nextInt();
            System.out.print("enter peocess no." + (i+1) + " brust time: ");
            p.brust_time = sc.nextInt();
            p.fixed_brust_time = p.brust_time ;
            p.priority = 0 ;

            System.out.print("enter peocess no." + (i+1) + " color: ");//2
            p.color = sc.next();

            arr.add(p);
            o.processes.add(p);//3
        }


        //sort the arr acording to brust time
        Collections.sort(arr,new Comparator<process>(){
            public int compare(process x,process y){
                return x.arrival_time - y.arrival_time ;
            }
        });



        PriorityQueue<process> pq = new PriorityQueue<process>(n,new MyComparator_brust_time());

        int    last_process_time = 0 ;
        boolean b = false ;
        String temp = "" ;
        double Average_Waiting_Time = 0 ;
        double Average_Turnaround_Time = 0 ;
        //execution of the processes
        while(!arr.isEmpty() || !pq.isEmpty())
        {

            //adding
            int num_of_deleting = 0 ;
            for (int i = 0 ; i < arr.size() ; i++)
            {
                if(arr.get(i).arrival_time <= last_process_time)
                {
                    pq.add(arr.get(i));//add the process at the pq
                    num_of_deleting++;
                }

            }

            for (int i = 0 ; i < num_of_deleting ; i++)
            {
                arr.remove(0);//delete the first element from arr
            }






            //loading
            if ((!temp.equals(pq.peek().name)) && b)
            {
                last_process_time+=c;//adding context time
                System.out.print("\ncontext time : last_process_time = " + last_process_time + "\n");
                System.out.println("\n\n");
                for (int i = 0 ; i < c ; i++)
                {
                    o.code.add("context");//4
                }

            }


            if (!b)//for first time only no context switch
            {
                b = true ;
            }






            //executing
            temp = pq.peek().name ;
            pq.peek().brust_time--;//excuting just one time unit



            last_process_time += 1 ;



            System.out.print(pq.peek().name + " : last_process_time = " + last_process_time + "\n");
            System.out.println("\n\n");


            //deleting
            if(pq.peek().brust_time == 0)//delete the element
            {
                int Turnaround_time =  last_process_time - pq.peek().arrival_time  ;
                int waiting_time = Turnaround_time - pq.peek().fixed_brust_time ;

                System.out.println("the process executed successfully!!!");
                System.out.println("\n\nname : " + pq.peek().name);
                System.out.println("brust_time : " + pq.peek().fixed_brust_time);
                System.out.println("arrival_time : " + pq.peek().arrival_time);
                System.out.println("waiting_time : " + waiting_time);
                System.out.println("Turnaround_time : " + Turnaround_time);
                System.out.println("\n\n");

                Average_Waiting_Time += waiting_time;
                if (!arr.isEmpty() || !pq.isEmpty())
                {
                    Average_Turnaround_Time += Turnaround_time;
                }

                pq.poll();//delete the element
            }


            //System.out.println("Average_Waiting_Time : " + Average_Waiting_Time);
            //System.out.println("Average_Turnaround_Time : " + Average_Turnaround_Time);
/*
        System.out.println("printing the array\n");
        for (int i = 0 ; i < arr.size() ; i++)
        {
            System.out.println("name : " + arr.get(i).name);
            System.out.println("brust_time : " + arr.get(i).brust_time);
        }
        System.out.println("\n\n");
*/

            o.code.add(temp);//4
        }
        System.out.println("final_Average_Waiting_Time = " + (Average_Waiting_Time/n));
        System.out.println("final_Average_Turnaround_Time = " + (Average_Turnaround_Time/n));

        return o ;//5

    }//srtf

    public static output priority()//done
    {
        Scanner sc = new Scanner (System.in) ;
        output o = new output() ;//1
        int n ;//number of processes
        System.out.print("enter number of processes you want to simulate :");
        n = sc.nextInt();

        //taking the processes
        ArrayList<process> arr = new ArrayList<process>();
        for (int i = 0 ; i < n ; i++)
        {
            process p = new process() ;
            System.out.print("enter peocess no." + (i+1) + " name: ");
            p.name = sc.next();
            System.out.print("enter peocess no." + (i+1) + " arrival time: ");
            p.arrival_time = sc.nextInt();
            System.out.print("enter peocess no." + (i+1) + " brust time: ");
            p.brust_time = sc.nextInt();
            System.out.print("enter peocess no." + (i+1) + " priority: ");
            p.priority = sc.nextInt();
            System.out.print("enter peocess no." + (i+1) + " color: ");//2
            p.color = sc.next();

            arr.add(p);
            o.processes.add(p);//3
        }


        //sort the arr acording to brust time
        Collections.sort(arr,new Comparator<process>(){
            public int compare(process x,process y){
                return x.arrival_time - y.arrival_time ;
            }
        });



        ArrayList<process> pq = new ArrayList<process>();

        int    last_process_time = 0 ;
        double Average_Waiting_Time = 0 ;
        double Average_Turnaround_Time = 0 ;
        //execution of the processes
        while(!arr.isEmpty() || !pq.isEmpty())
        {

            //adding
            int num_of_deleting = 0 ;
            for (int i = 0 ; i < arr.size() ; i++)
            {
                if(arr.get(i).arrival_time <= last_process_time)
                {
                    pq.add(arr.get(i));//add the process at the pq
                    num_of_deleting++;
                }

            }

            for (int i = 0 ; i < num_of_deleting ; i++)
            {
                arr.remove(0);//delete the first element from arr
            }

            //sorting
            //sort the pq acording to priority
            Collections.sort(pq,new Comparator<process>(){
                public int compare(process x,process y){
                    return x.priority - y.priority ;
                }
            });

            //executing

            process p = pq.get(0);
            pq.remove(0);


            int start_time ;


            ///calculate waiting
            if (last_process_time>p.arrival_time)///there is waiting here
            {
                start_time = last_process_time ;
            }
            else///there is no waiting
            {
                start_time = p.arrival_time ;
            }

            int waiting_time = start_time - p.arrival_time ;

            int Turnaround_time = start_time + p.brust_time ;


            last_process_time = Turnaround_time ;

            Average_Waiting_Time += waiting_time;

            if (!arr.isEmpty() || !pq.isEmpty())
            {
                Average_Turnaround_Time += Turnaround_time;
            }


            //solving starvation problem using Aging
            for (int i = 0 ; i < pq.size() ; i++)
            {
                if(pq.get(i).priority > 0)
                    pq.get(i).priority--;//increase the priority
            }



            System.out.println("name : " + p.name);
            System.out.println("brust_time : " + p.brust_time);
            System.out.println("arrival_time : " + p.arrival_time);
            System.out.println("priority : " + p.priority);
            System.out.println("start_time : " + start_time);
            System.out.println("waiting_time : " + waiting_time);
            System.out.println("Turnaround_time : " + Turnaround_time);
            System.out.println("\n\n");
            //System.out.println("Average_Waiting_Time : " + Average_Waiting_Time);
            //System.out.println("Average_Turnaround_Time : " + Average_Turnaround_Time);
/*
        System.out.println("printing the array\n");
        for (int i = 0 ; i < arr.size() ; i++)
        {
            System.out.println("name : " + arr.get(i).name);
            System.out.println("brust_time : " + arr.get(i).brust_time);
        }
        System.out.println("\n\n");
*/



            for (int i = 0 ; i < p.brust_time ; i++)
            {
                o.code.add(p.name);//4
            }

        }
        System.out.println("final_Average_Waiting_Time = " + (Average_Waiting_Time/n));
        System.out.println("final_Average_Turnaround_Time = " + (Average_Turnaround_Time/n));



        return o ;//5
    }//priority

    public static output AG()//done
    {
        Scanner sc = new Scanner (System.in) ;


        output o = new output() ;//1


        int n ;//number of processes
        System.out.print("enter number of processes you want to simulate :");
        n = sc.nextInt();


        //taking the processes into the array
        ArrayList<process> arr = new ArrayList<process>();
        ArrayList<process> arr2 = new ArrayList<process>();
        for (int i = 0 ; i < n ; i++)
        {
            process p = new process() ;
            System.out.print("enter peocess no." + (i+1) + " name: ");
            p.name = sc.next();
            System.out.print("enter peocess no." + (i+1) + " arrival time: ");
            p.arrival_time = sc.nextInt();
            System.out.print("enter peocess no." + (i+1) + " brust time: ");
            p.brust_time = sc.nextInt();
            p.fixed_brust_time = p.brust_time ;
            System.out.print("enter peocess no." + (i+1) + " priority: ");
            p.priority = sc.nextInt();
            System.out.print("enter peocess no." + (i+1) + " quantum: ");
            p.quantum = sc.nextInt();
            p.fixed_quantum = p.quantum ;


            System.out.print("enter peocess no." + (i+1) + " color: ");//2
            p.color = sc.next();


            p.ag = p.arrival_time + p.brust_time + p.priority ;


            arr.add(p);
            arr2.add(p);


            o.processes.add(p);//3
        }


        //sort the arr acording to brust time
        Collections.sort(arr,new Comparator<process>(){
            public int compare(process x,process y){
                return x.arrival_time - y.arrival_time ;
            }
        });


        ArrayList<process> list = new ArrayList<process> ();
        PriorityQueue<process> pq = new PriorityQueue<process>(n,new MyComparator_AG());



        int    last_process_time = 0 ;
        process last_process = arr.get(0);
        String temp = "" ;
        boolean u = false ;
        double Average_Waiting_Time = 0 ;
        double Average_Turnaround_Time = 0 ;


        //execution of the processes
        while(!arr.isEmpty() || !list.isEmpty())
        {

            //adding to the list
            int num_of_deleting = 0 ;
            for (int i = 0 ; i < arr.size() ; i++)
            {
                if(arr.get(i).arrival_time <= last_process_time)
                {
                    list.add(arr.get(i));//add the process at the list
                    num_of_deleting++;
                }

            }


            //delete the elements from arr
            for (int i = 0 ; i < num_of_deleting ; i++)
            {
                arr.remove(0);//delete the first element from arr
            }


            //update the pq from the list
            pq.clear();//clear the pq
            //add the elements into the pq
            for (int i = 0 ; i < list.size() ; i++)
            {
                pq.add(list.get(i));
            }



            //executing

            if(u)//for the first time
                last_process.brust_time--;//excuting just one time unit
            else
                u = true ;

            last_process.quantum--;//excuting just one quantam





            System.out.print(last_process.name + " : last_process_time = " + last_process_time + "\n");
            System.out.println("\n\n");

            last_process_time += 1 ;




            //scenarios

            /*arr2*/boolean w = false ;


            //first one : the process ended
            if (last_process.brust_time==0)//process ended
            {
                System.out.println("scenario one : the process ended");

              //quantum = 0
              last_process.fixed_quantum = 0 ;
              last_process.quantum = last_process.fixed_quantum ;

              //quantum = 0 in arr2

              for (int i = 0 ; i < arr2.size() ; i++)
              {
                  if(last_process.name.equals(arr2.get(i).name))
                  {
                      arr2.set(i,last_process);
                      break;
                  }
              }

              //remove the element from the list
              for (int i = 0 ; i < list.size() ; i++)
              {
                    if(last_process.name.equals(list.get(i).name))
                    {
                        list.remove(i);
                        break;
                    }
              }


              //calculate the waiting and the turnaround time
              int Turnaround_time =  last_process_time - last_process.arrival_time  ;
              int waiting_time = Turnaround_time - last_process.fixed_brust_time ;

              System.out.println("the process executed successfully!!!");
              System.out.println("\n\nname : " + last_process.name);
              System.out.println("brust_time : " + last_process.fixed_brust_time);
              System.out.println("arrival_time : " + last_process.arrival_time);
              System.out.println("priority : " + last_process.priority);
              System.out.println("AG : " + last_process.ag);
              System.out.println("waiting_time : " + waiting_time);
              System.out.println("Turnaround_time : " + Turnaround_time);
              System.out.println("\n\n");

                Average_Waiting_Time += waiting_time;
                if (!arr.isEmpty() || !list.isEmpty())
                {
                    Average_Turnaround_Time += Turnaround_time;
                }

              // new process -> the first element in list
              if(!list.isEmpty())
              {
                  last_process = list.get(0);
              }


              //printing the quantum of each process by arr2
              w = true ;
            }



            else if (last_process.quantum == 0)//second one : the process used all its quantum
            {
                System.out.println("scenario two : the process used all its quantum");
                //quantam+=ceil(10% of quantam)

                double d = last_process.fixed_quantum * 1.1 ;//quantum + 10%
                d = Math.ceil(d) ;//ceiling
                last_process.fixed_quantum = (int)(d) ;//fixed_quantum updated
                last_process.quantum = last_process.fixed_quantum ;

                //quantam+=ceil(10% of quantam) in arr2
                for (int i = 0 ; i < arr2.size() ; i++)
                {
                    if(last_process.name.equals(arr2.get(i).name))
                    {
                        arr2.set(i,last_process);
                        break;
                    }
                }

                //push the element at the end of arr
                list.add(last_process);

                //delete the same element from the array
                for (int i = 0 ; i < list.size() ; i++)
                {
                    if(last_process.name.equals(list.get(i).name))
                    {
                        list.remove(i);
                        break;
                    }
                }

                //new process -> the first element in list
                last_process = list.get(0);

                //printing the quantum of each process by arr2
                w = true ;
            }


            else if ((pq.peek().ag < last_process.ag)/*ag of pq is smaller*/&&(((double)(last_process.fixed_quantum-last_process.quantum)/(last_process.fixed_quantum)) >= 0.5)/*used more then 50% of its quantum*/)//third one : the process used some of if its quantam
            {
                System.out.println("scenario three : the process used some of if its quantam");

                //quantam+=the rest of last process
                last_process.fixed_quantum += last_process.quantum ;
                last_process.quantum = last_process.fixed_quantum ;

                //quantam+=the rest of last process in arr2
                for (int i = 0 ; i < arr2.size() ; i++)
                {
                    if(last_process.name.equals(arr2.get(i).name))
                    {
                        arr2.set(i,last_process);
                        break;
                    }
                }

                //push the element at the end of arr
                list.add(last_process);

                //delete the same element from the array
                for (int i = 0 ; i < list.size() ; i++)
                {
                    if(last_process.name.equals(list.get(i).name))
                    {
                        list.remove(i);
                        break;
                    }
                }

                //new process -> the pq.peek()
                last_process = pq.peek() ;

                //printing the quantum of each process by arr2
                w = true ;
            }

            /*
            else {
                //System.out.println("last_process.quantum = " + last_process.quantum);
                //System.out.println("last_process.fixed_quantum = " + last_process.fixed_quantum);
                //System.out.println(((double)(last_process.fixed_quantum-last_process.quantum)/(last_process.fixed_quantum)));
                System.out.println("no scenario\n\n\n\n\n\n");
            }
            */






        //printing the quantum of each process by arr2
        if(w)
        {

            System.out.println("n - a - b - p - q - AG");
            for (int i = 0 ; i < arr2.size() ; i++)
            {
                System.out.println(
                        arr2.get(i).name + " "
                         + arr2.get(i).arrival_time + " "
                         + arr2.get(i).brust_time + " "
                         + arr2.get(i).priority + " "
                         + arr2.get(i).fixed_quantum + " "
                         + arr2.get(i).ag + " "
                );
            }




            System.out.print("the quantum history!!!!!!!!!\n(");
            for (int i = 0 ; i < arr2.size() ; i++)
            {
                if (i != 0)
                {
                    System.out.print(",");
                }
                System.out.print(arr2.get(i).quantum);
            }
            System.out.print(")\n\n");
        }





        o.code.add(last_process.name);//4
        }//while
        System.out.println("final_Average_Waiting_Time = " + (Average_Waiting_Time/n));
        System.out.println("final_Average_Turnaround_Time = " + (Average_Turnaround_Time/n));


        return o ;//5

    }//AG


    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner (System.in) ;
        boolean w = true ;
        //welcome massege
        System.out.println("welcome to cpu Scheduling program , made by shihap, enjoy :)");
        while(w)
        {

            System.out.println("\n\n1-Non preemptive (SJF) Scheduling\n2-SRTF Scheduling\n3-Non-preemptive Priority Scheduling\n4-AG Scheduling\n0-exit\nenter the number of any choice to do it: ");
            int s = sc.nextInt() ;
            output o ;//for gui
            switch(s)
            {
                case 1://Non preemptive (SJF) Scheduling
                    o = sjf();
                    new Main(o);
                    break;

                case 2://SRTF Scheduling
                    o = srtf();
                    new Main(o);
                    break ;

                case 3://Non-preemptive Priority Scheduling
                    o = priority();
                    new Main(o);
                    break;

                case 4://AG Scheduling
                    o = AG();
                    new Main(o);
                    break;

                case 0://exit
                    System.out.println("thanks for using our program :)");
                    w = false ;
                    break;


                default://wrong input

                    System.out.println("wrong input, not found in the choices\n");

            }//switch

        }//while

    }//main

}//class







