/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mthree.codealongclassroster;

import controller.ClassRosterController;
import dao.ClassRosterDaoFileImpl;
import ui.ClassRosterView;
import ui.UserIO;
import ui.UserIOConsoleImpl;

/**
 *
 * @author conno
 */
public class App {
    
    public static void main(String[] args){
        
        ClassRosterController myController = new ClassRosterController(new ClassRosterView(new UserIOConsoleImpl()),new ClassRosterDaoFileImpl());
        
        myController.run();
        
    }
    
}
