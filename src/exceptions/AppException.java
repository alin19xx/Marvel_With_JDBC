/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Alex
 */
public class AppException extends Exception{
    // EXCEPTIONS WHICH WILL APPEAR IN GAME
    public static final String ERROR_001_Wrong_Command = "Wrong command";
    public static final String ERROR_002_Wrong_num_Args = "Wrong number of arguments";
    public static final String ERROR_003_User_Exists = "User already exists";
    public static final String ERROR_004_Superhero_Name_No_Exists = "There isn't a superhero with that name";
    public static final String ERROR_005_Wrong_Username_Pass = "Username or password is incorrect";
    public static final String ERROR_006_Not_Logged = "You are not logged in";
    public static final String ERROR_007_Not_Allowed_Direction = "You can't move in that direction";
    public static final String ERROR_008_Wrong_Gem_Name = "Here there is no gem with that name";
    public static final String ERROR_009_Finished_Game = "You already finish your game";
    public static final String ERROR_009_No_Enemy_Name_Place = "Here there is no enemy with that name";
    public static final String ERROR_010_Wrong_Pass_Deletion = "Delete aborted. Your password is wrong";
    public static final String ERROR_011_Not_Allowed_Deletion = "Delete aborted. You can't delete other user";
    public static final String ERROR_012_Not_Allowed_Null_Command = "You can't introduce an empty command";
    public static final String ERROR_013_General_Error = "Something has gone wrong, try again";
    public AppException(String message) {
        super(message);
    }
}
