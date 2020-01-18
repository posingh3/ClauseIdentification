/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clause;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
import java.util.*;

/**
 * 
 * @author Darshnik Swamy
 */


public class MiniProject2 {
    
    /**
     * @param args the command line arguments
     */
    
    static String tag = "";
    static String s1, s2, s3;
    static String parts[], pos[], fin[], tags[];
    static ArrayList<String> cue_phrases;
    static int having;
    
    /**
     * Stores all the cue phrases from the cue_phrases.csv
     * @throws Exception
     */
    
    public static void phrases() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("CuePhrases.csv"));
        String line = "";
        cue_phrases = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            cue_phrases.add(line);
        }
    }
    
    /*If the type of the sentence come into the division of COMPLEMENT CLAUSES */
    
    /**
     * If the type of the sentence come into the division of COMPLEMENT CLAUSES, takes the input of all the list of verbs.
     * @param verbs
     * verbs is the list of all the indices at which the verbs are present in the input sentence.
     * @return finale
     * , the output string after the sentence is divided into clauses.
    */
    
    public static String complement (ArrayList<Integer> verbs) {
        //"That" can be in the starting or in between.
        // if that is not in between then it cannot be an embedded clause
        
        String finale = "";
        ArrayList<Integer> fi = new ArrayList<>();
        ArrayList<String> clauses = new ArrayList<>();
        
        if (parts[0].equals("that")) {
             
            // if that is in between
            
            //checking for the first verb
            
              int i, verb = 0, mark = 0, full = 0, j;
              for (i = 1; i < tags.length; i++) {
                  if (tags[i].equals("VB") || tags[i].equals("VBP") || tags[i].equals("VBZ") || tags[i].equals("VBD") || tags[i].equals("VBN") || tags[i].equals("VBG")) {
                      verb = 1; break;
                  }
              }
              
              if (verb == 0) full = 1;
              
              //checking for the second verb(VB) or adjective (JJ)
              
              for (j = i+1; j < tags.length; j++) {
                  if(tags[j].equals("VB") || tags[j].equals("VBP") || tags[j].equals("VBZ") || tags[j].equals("VBD") || tags[j].equals("VBN") || tags[j].equals("VBG") || tags[j].equals("JJ")) {
                      verb = 2; break;
                  }
              }
              //System.out.println(j);
              if (verb == 0 || verb == 1) full = 1;
              
              //System.out.println(parts[i] + " " + parts[j]);
              if (full == 1) {
                  //cue_full_check();
                  //System.out.println("( " + s1 + " )");
                  
                  finale = finale + "(" + s1 + ")";
              } else {                                                      //finding the sub-clause according to a noun( NN) else the first clause is upto the first verb only  
                  int t, loop = 0;
                  for (t = i+1; t < j; t++) {
                      if (tags[t].equals("NN") || tags[t].equals("NNP") || tags[t].equals("NNPS") || tags[t].equals("NNS")) {
                          mark = t; loop = 1;
                          break;
                      }
                  }
                  if (loop != 1) {
                      mark = i;
                  }
                  ArrayList<Integer> br = new ArrayList<>();
                  br.add(mark);
                  fi = cue_check(br, verbs);
                  String s = "";
                for (int k = 0; k < fi.size(); k++) {
                    if(k == 0) {
                        for (int z = 0; z <= fi.get(k); z++) {
                            s = s + parts[z] + " ";
                        }
                    } else {
                        for (int z = fi.get(k-1)+1; z <= fi.get(k); z++) {
                            s = s + parts[z] + " ";
                        }
                    }
                    //System.out.println(s);
                    clauses.add(s);
                    s = "";
                }
                s = "";
                //System.out.println(s);
                for (int k = fi.get(fi.size()-1)+1; k < parts.length; k++) {
                    s = s + parts[k] + " ";
                }
                //System.out.println(s);
                clauses.add(s);
                s = "";
                finale += "(";
                for (int k = 0; k < clauses.size()-1; k++) {
                    finale = finale + clauses.get(k) + ") (";
                }
                finale = finale + clauses.get(clauses.size()-1) + ")";
              }
          } else {
              //sSystem.out.println("sdfdssd");
              int i = 0, embedded = 0, verb = 0, noun = 1, pronoun = 1;
              
              //finding all the verbs and nouns or pronouns
             // System.out.println("sdfsd");
              while (!parts[i].equals("that")) {
                  if (tags[i].equals("VBP") || tags[i].equals("VB") || tags[i].equals("VBD") || tags[i].equals("VBN") || tags[i].equals("VBG") || tags[i].equals("VBZ")) {
                      verb = 1;
                  }
                  if (tags[i].equals("NN") || tags[i].equals("NNP") || tags[i].equals("NNPS") || tags[i].equals("NNS")) {
                      noun = 1;
                  }
                  if (tags[i].equals("PRP") || tags[i].equals("PRP$")) {
                      pronoun = 1;
                  }
                  i++;
              }
              //System.out.println(i);
              // if the there is noun and verb or pro. and verb before that then it will not be an embedded clause.
              
              if ((noun == 1 && verb == 1) || (pronoun == 1 && verb == 1)) embedded = 0;
              else embedded = 1;
              System.out.println(embedded);
              //System.out.println(tags[i-2]);
              
              //not embedded
              if (embedded == 0) {
                 // System.out.println("sdfds");
                  //System.out.println(tags[i-2]);
                  ArrayList<Integer> br = new ArrayList<>();
                  int x = i;
                  int full = 1;
                  for (int j = 0; j < verbs.size(); j++) {
                        if (verbs.get(j) > x) {
                            full = 0; break;
                        }
                    }    
                if (full == 0) {                    
                    br.add(i);
                    fi = cue_check(br, verbs);
                    
                } else {
                    //System.out.println("sfdsdfsd");
                    fi = cue_check_full(verbs);
                    //finale = finale + "(" + s1 + ")";
                    //System.out.println(finale);
                }
                
                if (fi.isEmpty()) finale = finale + "(" + s1 + ")";
                else {
                      String s = "";
                    for (int k = 0; k < fi.size(); k++) {
                        if(k == 0) {
                            for (int z = 0; z <= fi.get(k); z++) {
                                s = s + parts[z] + " ";
                            }
                        } else {
                            for (int z = fi.get(k-1)+1; z <= fi.get(k); z++) {
                                s = s + parts[z] + " ";
                            }
                        }
                        //System.out.println(s);
                        clauses.add(s);
                        s = "";
                    }
                    s = "";
                    //System.out.println(s);
                    for (int k = fi.get(fi.size()-1)+1; k < parts.length; k++) {
                        s = s + parts[k] + " ";
                    }
                    
                //System.out.println(s);
                    clauses.add(s);
                    s = "";
                    finale += "(";
                    for (int k = 0; k < clauses.size()-1; k++) {
                        finale = finale + clauses.get(k) + ") (";
                    }
                    finale = finale + clauses.get(clauses.size()-1) + ")";
              
                }
                
              } else { // embedded
                  fi = cue_check_full(verbs);
                  if (fi.isEmpty()) finale = finale + "(" + s1 + ")";
                  else {
                      String s = "";
                    for (int k = 0; k < fi.size(); k++) {
                        if(k == 0) {
                            for (int z = 0; z <= fi.get(k); z++) {
                                s = s + parts[z] + " ";
                            }
                        } else {
                            for (int z = fi.get(k-1)+1; z <= fi.get(k); z++) {
                                s = s + parts[z] + " ";
                            }
                        }
                        //System.out.println(s);
                        clauses.add(s);
                        s = "";
                    }
                    s = "";
                    //System.out.println(s);
                    for (int k = fi.get(fi.size()-1)+1; k < parts.length; k++) {
                        s = s + parts[k] + " ";
                    }
                    
                //System.out.println(s);
                    clauses.add(s);
                    s = "";
                    finale += "(";
                    for (int k = 0; k < clauses.size()-1; k++) {
                        finale = finale + clauses.get(k) + ") (";
                    }
                    finale = finale + clauses.get(clauses.size()-1) + ")";
              
                }
              }
          }
          
        return finale;
        
    }
    
    /**
     * check_participle method takes the index of gerund of the v3 form of the verb from the sentence and divide the sentence accordingly
     * @param gerund_index
     * gerund_index is the index of VBG in the sentence.
     * @param verbs
     * verbs is the list of the indices of the verbs present in the sentence
     * @return finale
     * , the output string after the sentence is divided into clauses.
     */
    
    public static String check_participle(int gerund_index, ArrayList<Integer> verbs) {
        String finale = "";
        ArrayList<Integer> fi = new ArrayList<>();
        ArrayList<String> clauses = new ArrayList<>();
        
        if (gerund_index == 0) {
                //System.out.println("sdfds");
                int i = 1, found = 0;
                if (parts[0].equals("having")) i = 2;
                
                while (i < tags.length-1) {
                    if (tags[i].equals("NN") || tags[i].equals("NNS") || tags[i].equals("RB") || tags[i].equals("RP") || tags[i].equals("VB") || tags[i].equals("VBG") || tags[i].equals("VBN") || tags[i].equals("VBZ") || tags[i].equals("VBD") || tags[i].equals("VBP") || tags[i].equals("PRP")) {
                        if (tags[i+1].equals("PRP$") || tags[i+1].equals("PRP") || (tags[i+1].equals("DT") && (tags[i+2].equals("NN") || tags[i+2].equals("NNS"))) || tags[i+1].equals("NN") || tags[i+1].equals("NNS") || tags[i+1].equals("NNP") || tags[i+1].equals("NNPS")) {
                            found = 1;
                            break;
                        }
                    }
                    
                    if (tags[i].equals("JJ") || tags[i].equals("JJR") || tags[i].equals("JJS")) {
                        if (tags[i+1].equals("NNP") || (tags[i+1].equals("DT") && (tags[i+2].equals("NN") || tags[i+2].equals("NNS"))) || tags[i+1].equals("PRP") || tags[i+1].equals("PRP$")) {
                            found = 1;
                            break;
                        }
                    }
                    i++;
                }
               // System.out.println(found);
                if (found == 1) {
                    ArrayList<Integer> br = new ArrayList<Integer>();
                    br.add(i);
                    //System.out.println("sdf");
                    fi = cue_check(br, verbs);
                   
                } else {
                    //System.out.println("sdf");
                    fi = cue_check_full(verbs);
                    //finale = finale + "( " + s1 + " )";
                }                
                if(fi.isEmpty()) finale = finale + "( " + s1 + " )";
                else { 
                    String s = "";
                    for (int k = 0; k < fi.size(); k++) {
                        if(k == 0) {
                            for (int z = 0; z <= fi.get(k); z++) {
                                s = s + parts[z] + " ";
                            }
                        } else {
                            for (int z = fi.get(k-1)+1; z <= fi.get(k); z++) {
                                s = s + parts[z] + " ";
                            }
                        }
                        //System.out.println(s);
                        clauses.add(s);
                        s = "";
                    }
                    s = "";
                    //System.out.println(s);
                    for (int k = fi.get(fi.size()-1)+1; k < parts.length; k++) {
                        s = s + parts[k] + " ";
                    }
                    //System.out.println(s);
                    clauses.add(s);
                    s = "";
                    finale += "(";
                    for (int k = 0; k < clauses.size()-1; k++) {
                        finale = finale + clauses.get(k) + ") (";
                    }
                    finale = finale + clauses.get(clauses.size()-1) + ")";
                }
            } else {
                int i = 0, verb = 0, embedded = 0;
                while (i < gerund_index) {
                    if (tags[i].equals("VBP") || tags[i].equals("VB") || tags[i].equals("VBD") || tags[i].equals("VBN") || tags[i].equals("VBZ")) {
                        verb = 1; break;
                    }
                    i++;
                }
                
                if (verb == 0) embedded  = 1;
               // System.out.println(embedded);       
                if (embedded == 0) {
                    //System.out.println("sdf");
                    int full = 1;
                    if (tags.length-1 != gerund_index) {
                       /* if (tags[gerund_index+1].equals("DT") || tags[gerund_index+1].equals("NN") || tags[gerund_index+1].equals("NNS") || tags[gerund_index+1].equals("NNP") || tags[gerund_index+1].equals("NNPS") || tags[gerund_index+1].equals("RP") || tags[gerund_index+1].equals("RB") || tags[gerund_index+1].equals("PRP")) {
                           full = 1;
                        }*/
                        for (int j = 0; j < verbs.size(); j++) {
                            if (verbs.get(j) > gerund_index) {
                                full = 0; break;
                            }
                        }                                                
                    }
                    
                    //if (gerund_index == tags.length-1) full = 1;
                    //System.out.println("sdfsdf");
                    if (full == 1) {
                        //System.out.println("sdfsd");
                        fi = cue_check_full(verbs);
                        //System.out.println(finale);
                    } else {
                        ArrayList<Integer> br = new ArrayList<>();
                        br.add(gerund_index);
                        fi = cue_check(br, verbs);
                    } 
                    
                    if (fi.isEmpty()) {
                        finale = finale + "(" + s1 + ")";
                    } else {
                        String s = "";
                        for (int k = 0; k < fi.size(); k++) {
                            if(k == 0) {
                                for (int z = 0; z <= fi.get(k); z++) {
                                    s = s + parts[z] + " ";
                                }
                            } else {
                                for (int z = fi.get(k-1)+1; z <= fi.get(k); z++) {
                                    s = s + parts[z] + " ";
                                }
                            }
                            //System.out.println(s);
                            clauses.add(s);
                            s = "";
                        }
                        s = "";
                        //System.out.println(s);
                        for (int k = fi.get(fi.size()-1)+1; k < parts.length; k++) {
                            s = s + parts[k] + " ";
                        }
                        //System.out.println(s);
                        clauses.add(s);
                        s = "";
                        finale += "(";
                        for (int k = 0; k < clauses.size()-1; k++) {
                            finale = finale + clauses.get(k) + ") (";
                        }
                        finale = finale + clauses.get(clauses.size()-1) + ")";
                    }
                } else {
                    int x = gerund_index, found = 0;
                    while (x < tags.length-1) {
                        //System.out.println("sdfsdfds");
                        if (tags[x].equals("NN") || tags[x].equals("NNS") || tags[x].equals("RB") || tags[x].equals("RP") || tags[x].equals("VB") || tags[x].equals("VBG") || tags[x].equals("VBN") || tags[x].equals("VBZ") || tags[x].equals("VBD") || tags[x].equals("VBP") || tags[x].equals("PRP")) {
                            if (tags[x+1].equals("PRP") || tags[x+1].equals("NN") || tags[x+1].equals("NNS") || tags[x+1].equals("NNP") || tags[x+1].equals("NNPS") || tags[x+1].equals("MD") || tags[x+1].equals("VB") || tags[x+1].equals("VBZ") || tags[x+1].equals("VBP") || tags[x+1].equals("VBD")) {
                                found = x;
                                break;
                            }
                        }
                        
                        if (tags[x].equals("JJ") || tags[x].equals("JJR") || tags[x].equals("JJS")) {
                            if (tags[x+1].equals("NNP") || (tags[x+1].equals("DT") && (tags[x+2].equals("NN") || tags[x+2].equals("NNS"))) || tags[x+1].equals("PRP")) {
                                found = x;
                                break;
                            }
                        }
                        x++;
                    }
                    //System.out.println(found);
                    
                    x = 0;
                    finale += "(";
                    while (x < gerund_index) {
                        finale = finale +  parts[x++] + " ";
                    }
                    
                    finale += "(";
                    while(x <= found) {
                        finale = finale + parts[x++] + " ";
                    }
                    finale += ")";
                    while (x < tags.length) {
                        finale = finale + parts[x++] + " ";
                    }
                    finale += ")";
                }
            }        
        return finale;
    }
    
    /**
     * participle method checks for the indices of VBG and VBN tags in the sentence and call the check_participle method according to the verb phrases in which these tags are dividing the sentence into.
     * @param gerund
     * gerund is the index of VBG in the sentence.
     * @param v3
     * v3 is the index of VBN in the sentence
     * @param verbs
     * verbs is the list of all the types of verbs present in the sentence
     * @return finale
     * , the output string after the sentence is divided into clauses.
     */
    
    public static String participle(int gerund, int v3, ArrayList<Integer> verbs) {
        
        String finale = "";
        int gerund_index = 0, v3_index = 0;
        
        //System.out.println(tags[0]);
        if (tags[0].equals("VBG")) gerund_index = 0;
        else gerund_index = -1;
        
        if (tags[0].equals("VBN")) v3_index = 0;
        else v3_index = -1;
        
        if (gerund_index != 0) {
            //System.out.println("sdfsd");
            for (int i = 0; i < tags.length; i++) {
                if (tags[i].equals("VBG")) gerund_index = i;
            }
        }
        
        if (v3_index != 0) {
            for (int i = 0; i < tags.length; i++) {
                if (tags[i].equals("VBN")) v3_index = i;
            }
        }
        
        if (gerund == 1 && v3 == 0) {
           // System.out.println("sfsdsdf");
            finale = check_participle(gerund_index, verbs);
        } else if (v3 == 1 && gerund == 0) {
            finale = check_participle(v3_index, verbs);
        } else if (gerund == 1 && v3 == 1) {
            if (gerund_index < v3_index) {
                finale = check_participle(gerund_index, verbs);
            } else {
               finale = check_participle(v3_index, verbs);
            }
        }
        
        return finale;
    }
    
    /**
     * comma_check is the method which takes the indices of all the commas present in the sentence and check if it divides the sentences into clauses on the basis of checking the verb-phrases
     * @param mark
     * mark is the list of all the commas present in the sentence.
     * @param verbs
     * verbs is the list of all the indices present in the sentence.
     * @return br
     * br is the list of indices of the final set of commas which are sure to divide the sentence into clauses.
     */
    public static ArrayList<Integer> comma_check (ArrayList<Integer> mark, ArrayList<Integer> verbs) {
        int low = -1, v1 = 0, v2 = 0;
        ArrayList<Integer> br = new ArrayList<>();
        for (int i = 0; i < mark.size(); i++) {
            v1 = v2 = 0;
            for (int j = 0; j < verbs.size(); j++) {
                if (verbs.get(j) >= low+1 && verbs.get(j) <= mark.get(i)) {
                    v1 = 1;
                }
                if (verbs.get(j) > mark.get(i) && verbs.get(j) < tags.length) {
                    v2 = 1;
                }
            }
            if (v1 == 1 && v2 == 1) {
                //System.out.println("sdfdsf");
                low = mark.get(i);
                br.add(mark.get(i));
            }
        }
        
        return br;
    }
    
    /**
     * cue_check is the method which checks clauses in all the divided clauses by the comma_check method on the basis of cue phrases which takes into account all the verb phrases. 
     * @param mark
     * mark is the list of indices of all the commas which are dividing the sentences into clauses.
     * @param verbs
     * verbs is the list of indices of all the verbs of the sentences 
     * @return cu
     * cu is the list of the indices (collected and sorted) after all the cue_phrases are checked.
     */
    
    public static ArrayList<Integer> cue_check(ArrayList<Integer> mark, ArrayList<Integer> verbs) {
        int low = -1;
        ArrayList<Integer> cu = new ArrayList<>();
        for (int i = 0; i < mark.size(); i++) {
            cu.add(mark.get(i));
        }
        Set<Integer> cue;
        for (int i = 0; i < mark.size(); i++) {
            cue = new HashSet<Integer>();
            int high = mark.get(i);
            for (int j = 0; j < cue_phrases.size(); j++) {
                String s = cue_phrases.get(j);
                String[] p = s.split(" ");
                int si = p.length;
                int st = low +1;
                for (int k = 1; k <= (high - low - si + 1); k++) {
                    int sti = st;
                    int eq = 0;
                    for (int x = 0; x < si; x++) {
                        if (!p[x].equals(parts[sti])) {
                            eq = -1; break;
                        }
                        sti++;
                    }
                    
                    if (eq != -1) {
                        cue.add(sti-1);
                       // break;
                        st++;
                    } else st++;
                }
            }
            
            ArrayList to =  new ArrayList();
            for (Integer x : cue) {
                to.add(x);
            }
            for (int k = 0; k < to.size(); k++) {
                for (int j = k+1; j < to.size(); j++) {
                    if ((Math.abs((Integer)to.get(j) - (Integer)to.get(k)) == 2) || (Math.abs((Integer)to.get(j) - (Integer)to.get(k)) == 1)) {
                        if ((Integer)to.get(j) < (Integer)to.get(k)) {
                            cue.remove(to.get(j));
                        } else cue.remove(to.get(k));
                    }
                }
            }
            
            Iterator it = cue.iterator();
            int l = low;
            while (it.hasNext()) {
                int temp = (Integer)it.next();
                int v1 = 0, v2 = 0;
                for (int k = 0; k < verbs.size(); k++) {
                    if (verbs.get(k) >= l+1 && verbs.get(k) <= temp) {
                        v1 = 1;
                    }
                    if (verbs.get(k) > temp && verbs.get(k) <= high) {
                        v2 = 1;
                    }
                }
                if (v1 == 1 && v2 == 1) {
                    cu.add(temp);
                    l = temp;
                }
            }
            low = high;
        }
        Set<Integer> n = new HashSet<Integer>();
        int high = parts.length-1;
        for (int j = 0; j < cue_phrases.size(); j++) {
            String s = cue_phrases.get(j);
            String[] p = s.split(" ");
            int si = p.length;
            int st = low + 1;
            for (int k = 1; k <= (high - low - si + 1); k++) {
                int sti = st;
                int eq = 0;
                for (int x = 0; x < si; x++) {
                    if (!p[x].equals(parts[sti])) {
                        eq = -1; break;
                    }
                    sti++;
                }
                
                if (eq != -1) {
                   // System.out.println("sdfsdfsdfs");
                    n.add(sti-1);
                    //break;
                } else st++;
            }
        }
        
       /* Iterator<Integer> it1 = n.iterator();
        while (it1.hasNext()) {
            Iterator<Integer> it2 = n.iterator();
            Integer x = it1.next();
            int x1 = (Integer)x;
            while (it2.hasNext()) {
                x = it2.next();
                int x2 = (Integer)x;
                if (Math.abs(x1 - x2) == 2) {
                    if (x1 > x2) {
                        n.remove(x2);
                    } else n.remove(x1);
                }
            }
        }*/
        
        ArrayList to =  new ArrayList();
        for (Integer x : n) {
            to.add(x);
        }
        //System.out.println(to);
        for (int i = 0; i < to.size(); i++) {
            for (int j = i+1; j < to.size(); j++) {
                if ((Math.abs((Integer)to.get(j) - (Integer)to.get(i)) == 2) || (Math.abs((Integer)to.get(j) - (Integer)to.get(i)) == 1)) {
                    if ((Integer)to.get(j) < (Integer)to.get(i)) {
                        n.remove(to.get(j));
                        
                    } else n.remove(to.get(i));
                }
            }
        }
        
        //System.out.println(n);
        Iterator it = n.iterator();
        int l = low;
        while (it.hasNext()) {
            int temp = (Integer)it.next();
            int v1 = 0, v2 = 0;
            for (int k = 0; k < verbs.size(); k++) {
                if (verbs.get(k) >= l+1 && verbs.get(k) <= temp) {
                    v1 = 1;
                }
                if (verbs.get(k) > temp && verbs.get(k) <= high) {
                    v2 = 1;
                }
            }
            if (v1 == 1 && v2 == 1) {
                cu.add(temp);
                l = temp;
            }
        }
        //System.out.println(cu);
        Collections.sort(cu);
        //System.out.println(cu);
        return cu;
    }
    
   /**
     * This method checks for the whole sentences for cue_phrases on the basis of all indices of verbs.
     * @param verbs
     * verbs is the list of indices of the verbs in the sentence.
     * @return cu
     * , the final list of the indices which will divide the sentence into clauses.
     */
    
    public static ArrayList<Integer> cue_check_full(ArrayList<Integer> verbs) {
        ArrayList<Integer> cu = new ArrayList<>();
        int low = -1;
        Set<Integer> n = new HashSet<Integer>();
        int high = parts.length-1;
        for (int j = 0; j < cue_phrases.size(); j++) {
            String s = cue_phrases.get(j);
            String[] p = s.split(" ");
            int si = p.length;
            int st = low + 1;
            for (int k = 1; k <= (high - low - si + 1); k++) {
                int sti = st;
                int eq = 0;
                for (int x = 0; x < si; x++) {
                    if (!p[x].equals(parts[sti])) {
                        eq = -1; break;
                    }
                    sti++;
                }
                   
                if (eq != -1) {
                   // System.out.println("sdfsdfsdfs");
                    n.add(sti-1);
                    //break;
                } else st++;
            }
        }
        
       /* Iterator<Integer> it1 = n.iterator();
        while (it1.hasNext()) {
            Iterator<Integer> it2 = n.iterator();
            Integer x = it1.next();
            int x1 = (Integer)x;
            while (it2.hasNext()) {
                x = it2.next();
                int x2 = (Integer)x;
                if (Math.abs(x1 - x2) == 2) {
                    if (x1 > x2) {
                        n.remove(x2);
                    } else n.remove(x1);
                }
            }
        }*/
        
        ArrayList to =  new ArrayList();
        for (Integer x : n) {
            to.add(x);
        }
        
        //System.out.println(to);
        for (int i = 0; i < to.size(); i++) {
            for (int j = i+1; j < to.size(); j++) {
                if ((Math.abs((Integer)to.get(j) - (Integer)to.get(i)) == 2) || (Math.abs((Integer)to.get(j) - (Integer)to.get(i)) == 1)) {
                    if ((Integer)to.get(j) < (Integer)to.get(i)) {
                        n.remove(to.get(j));
                        
                    } else n.remove(to.get(i));
                }
            }
        }
        
        //System.out.println(n);
        Iterator it = n.iterator();
        int l = low;
        while (it.hasNext()) {
            int temp = (Integer)it.next();
            int v1 = 0, v2 = 0;
            for (int k = 0; k < verbs.size(); k++) {
                if (verbs.get(k) >= l+1 && verbs.get(k) <= temp) {
                    v1 = 1;
                }
                if (verbs.get(k) > temp && verbs.get(k) <= high) {
                    v2 = 1;
                }
            }
            if (v1 == 1 && v2 == 1) {
                cu.add(temp);
                l = temp;
            }
        }
        //System.out.println(cu);
        Collections.sort(cu);
        //System.out.println(cu);
        return cu;
    }
    
    /**
     * This method divides the sentence into clauses.
     * @param s5
     * s5 is the input string for which clause identification is to be done.
     * @return finale
     * , the output string which is divided on the basis of clauses present in the sentence.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws Exception
     */
    
    public static String main(String s5) throws FileNotFoundException, IOException, Exception {
        MaxentTagger tagger = new MaxentTagger(
                "stanford-postagger-full-2014-01-04/models/english-left3words-distsim.tagger");
        phrases();
        Scanner in  = new Scanner(System.in);
        //s1 = in.nextLine();
        s1 = s5;
        s3 = s1;
        //System.out.println(s3);
        s3 = s3.replaceAll("[,.]", "");
        //s3 = s3.replaceAll(".", "");
        //System.out.println(s3);
        tag = tagger.tagString(s3);
        s2 = s1.toLowerCase();
        s2 = s2.replaceAll("[.]", "");
        //System.out.println(s2);
        parts = s2.split(" ");
        fin = s1.split(" ");
        //System.out.println(tag);
        pos = tag.split(" ");
        
        /* splitting the tags */
        
        tags = new String[pos.length];
        //parts = new String[pos.length];
        //parts = s2.split(" ");
        //System.out.println(parts.length);
        
        for (int i = 0; i < pos.length; i++) {
            String[] p;
            p = pos[i].split("_");
            //words[i] = p[0]; 
            tags[i] = p[1];
        }
        
        //for (int i = 0; i < parts.length; i++) 
        //    System.out.println(parts[i]);
        
        /*detecting the gerund and the third form of the verb for PARTICIPLE CLAUSES */
        
        
        /*dividing on the basis of comma */
        ArrayList<Integer> mark = new ArrayList<>();
        ArrayList<Integer> verbs = new ArrayList<>();
        ArrayList<String> clauses = new ArrayList<>();
        ArrayList<Integer> br = new ArrayList<>();
        //mark.add(-1);
        int low = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains(",")) {
                mark.add(i);
            }
        }
        
        //for (int i = 0; i < mark.size(); i++) System.out.println(mark.get(i));
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].equals("VB") || tags[i].equals("VBZ") || tags[i].equals("VBP") || tags[i].equals("VBG") || tags[i].equals("VBD") || tags[i].equals("VBN")) {
                verbs.add(i);
            }
        }
        
        String finale = "";
        ArrayList<Integer> fi = new ArrayList<>();
        int comma = 1;
        if (!mark.isEmpty()) {
           // System.out.println("sdfds");
            //System.out.println(mark);
            br = comma_check(mark, verbs);
           // System.out.println(br);
            //System.out.println(verbs);
            //for (int i = 0; i < br.size(); i++) System.out.println(br.get(i));
            if (!br.isEmpty()) {
                String s = "";
                //clauses = new ArrayList<>();
                fi = cue_check(br, verbs);
                for (int i = 0; i < fi.size(); i++) {
                    if(i == 0) {
                        for (int j = 0; j <= fi.get(i); j++) {
                            s = s + parts[j] + " ";
                        }
                    } else {
                        for (int j = fi.get(i-1)+1; j <= fi.get(i); j++) {
                            s = s + parts[j] + " ";
                        }
                    }
                    //System.out.println(s);
                    clauses.add(s);
                    s = "";
                }
                s = "";
                //System.out.println(s);
                for (int i = fi.get(fi.size()-1)+1; i < parts.length; i++) {
                    s = s + parts[i] + " ";
                }
                //System.out.println(s);
                clauses.add(s);
                s = "";
                finale += "(";
                for (int i = 0; i < clauses.size()-1; i++) {
                    finale = finale + clauses.get(i) + ") (";
                }
                finale = finale + clauses.get(clauses.size()-1) + ")";
            } else comma = 0;
        } else {
            comma = 0;
        }
        
        /*checking for different clauses*/
        
        int gerund = 0, v3 = 0;          
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].equals("VBG")) {
                gerund = 1; 
            }
            if (tags[i].equals("VBN")) {
                v3 = 1;
            }
        }
        
        /*detecting if the sentence belongs to CONDITIONAL CLAUSES*/
        
        int conditional = 0;
        
        if (parts[0].equals("should") || parts[0].equals("had") || parts[0].equals("were") || parts[0].equals("with")) {
            conditional = 1;
        }
        if (s2.contains("if") || s2.contains("otherwise") || s2.contains("unless") || s2.contains("as long as") || s2.contains("so long as") || s2.contains("as soon as") || s2.contains("supposing") || s2.contains("on condition that") || s2.contains("in case") || s2.contains("in case of")) {
            conditional = 1;
        }
        
        /*calling the specific functions according to the rules*/
        
        if (comma == 0) {
            if (s2.contains("that") || s2.contains("just that")) {
                finale = complement(verbs);      
            }
            else if ((gerund == 1 || v3 == 1) && conditional != 1) {
                //System.out.println("sfdsd");
                finale = participle(gerund, v3, verbs);
            } else {
                fi = cue_check_full(verbs);
                if (fi.isEmpty()) {
                    finale = finale + "(" + s1 + ")";
                } else {
                    String s = "";
                    for (int i = 0; i < fi.size(); i++) {
                        if(i == 0) {
                            for (int j = 0; j <= fi.get(i); j++) {
                                s = s + parts[j] + " ";
                            }
                        } else {
                            for (int j = fi.get(i-1)+1; j <= fi.get(i); j++) {
                                s = s + parts[j] + " ";
                            }
                        }
                        //System.out.println(s);
                        clauses.add(s);
                        s = "";
                    }
                    s = "";
                    //System.out.println(s);
                    for (int i = fi.get(fi.size()-1)+1; i < parts.length; i++) {
                        s = s + parts[i] + " ";
                    }
                    //System.out.println(s);
                    clauses.add(s);
                    s = "";
                    finale += "(";
                    for (int i = 0; i < clauses.size()-1; i++) {
                        finale = finale + clauses.get(i) + ") (";
                    }
                    finale = finale + clauses.get(clauses.size()-1) + ")";
                }
            }
           // System.out.println(finale);
        }
        
        //System.out.println(finale);
        return finale;
    }
}


