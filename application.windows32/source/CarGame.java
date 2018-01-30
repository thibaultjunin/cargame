import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CarGame extends PApplet {

/*
 * Car Simulator
 */
PImage road_one,road_two,carPlayer;
int carX = 341;
int nbCars = -1;
float[][] cars,nvCars;
float[] lanes;
int currentFrame = 0;
boolean game = true;

// Initilisation
public void setup(){
  frameRate(60);
  road_one = loadImage("Road S1.png");
  road_two = loadImage("Road S2.png");
  carPlayer = loadImage("Black_viper.png");
  cars = new float[0][5];
  nvCars = new float[0][5];
  lanes = new float[3];
  lanes[0] = 100;
  lanes[1] = 240;
  lanes[2] = 390;
  spawnNPC();
  
}

// Boucle de contr\u00f4le du jeu
public void draw(){
  if(!game){
    PImage end;
    end = loadImage("END.png");
    background(end);
    return; 
  }
  if(currentFrame >= 60){
    currentFrame = 0;
  }
  currentFrame++;
  background(((currentFrame > 15 && currentFrame < 30) || (currentFrame > 45 && currentFrame < 60)) ? road_two : road_one);
  image(carPlayer, carX, height-256);
  if(cars[nbCars][1] > 600){
    spawnNPC();
  }
  moveCars();
  showCars();
}

// Quand tu touche est appuy\u00e9
public void keyPressed(){
  if(key == CODED){
    if(keyCode == LEFT){
      if(carX < 75){
        return;
      }
      carX = carX - 7;
    }
    if(keyCode == RIGHT){
      if(carX > 418){
        return;
      }
      carX = carX + 7;
    }
  }
}

// Fait appraitre une voiture non joueuse
public void spawnNPC(){
  int dir  = (int) random(1, 2);
  int lane = (int) random(0, 3);
  if(lane == 0){
    dir = 2;
  }
  if(lane == 3){
    lane = 2;
  }
  nbCars = nbCars + 1;
  print(nbCars+"\n");
  nvCars = new float[nbCars+1][5];
  for(int i = 0; i < nbCars-1; i++){
    print("1. DIR: "+cars[i][3]+"\n");
    nvCars[i] = cars[i];
  }
  cars   = null;
  cars   = new float[nbCars+1][5];
  for(int i = 0; i < nbCars-1; i++){
    cars[i] = nvCars[i];
  }
  float modifier = random(-20, 30);
  cars[nbCars][0] = lanes[lane]+modifier;
  cars[nbCars][1] = -256;
  cars[nbCars][2] = random(0, 7);
  cars[nbCars][3] = dir;
  cars[nbCars][4] = random(1, 3);
}

// Affiche les voitures non joueur
public void showCars(){
  for(int i = 0; i < nbCars+1; i++){
    if(cars[i][1] != 9999){
      int model = (int)cars[i][2];
      int dir   = (int)cars[i][3];
      if(dir != 0){
        PImage carToShow;
        carToShow = loadImage("car_"+model+"_"+dir+".png");
        image(carToShow, cars[i][0], cars[i][1]);
        if(overlap(carToShow, carPlayer, (int)cars[i][0], (int)cars[i][1], (int)carX, (int)height-256)){
          game = false;
        }else{
          game = true;
        }
      }      
    }
  }
}

// Deplace les oitures non joueur
public void moveCars(){
  for(int i = 0; i < nbCars+1; i++){
    if(cars[i][1] != 9999){
      cars[i][1] = cars[i][1]+cars[i][4];
    }
    if(cars[i][1] > 700){
      cars[i][1] = 9999;
    }
  }
}

// Verifie si deux voiture se touche
public boolean overlap(PImage p1, PImage p2, int x1, int y1, int x2, int y2){
  if(within(x2, x1, getBx(x1, p1)) || within(getFx(x2, p2), x1, getBx(x1, p1))){
    if(within(y2, y1, getCy(y1, p1)) || within(getGy(y2, p2), y1, getCy(y1, p1))){
      return true;
    }
  }
  return false;
}

public int getBx(int Ax,  PImage image){
  int bx = Ax+image.width;
  return bx;
}

public int getCy(int Ay, PImage image){
  int cy = Ay+image.height;
  return cy;
}

public int getFx(int Ex, PImage image){
  int ex = Ex+image.width;
  return ex;
}

public int getGy(int Ey, PImage image){
  int gy = Ey+image.height;
  return gy;
}

public boolean within(int a, int b, int c){
  return (a>b && a<c);
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CarGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
