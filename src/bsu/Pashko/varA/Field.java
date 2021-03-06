package bsu.Pashko.varA;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Field extends JPanel {
    // Флаг приостановленности движения
    // paused - все мячи,pausedSmall - маленькие,pausedLarge - большие.
    private boolean paused;
    private boolean pausedSmall;
    private boolean pausedLarge;
    private boolean pausedFast;
    private boolean pausedSlow;
    private boolean pausedTrajectory1;
    private boolean pausedTrajectory2;
    private boolean pausedTrajectory3;
    private boolean pausedTrajectory4;
    private boolean pausedRed;
    private boolean pausedGreen;
    private boolean pausedBlue;
    private int maxStop = 5;

    // Динамический список скачущих мячей
    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    // Класс таймер отвечает за регулярную генерацию событий ActionEvent
    //реализующий интерфейс ActionListener
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
// Задача обработчика события ActionEvent - перерисовка окна
            repaint();
        }
    });
    // Конструктор класса Field
    public Field() {
// Установить цвет заднего фона белым
        setBackground(Color.WHITE);
// Запустить таймер
        repaintTimer.start();
    }
    // Унаследованный от JPanel метод перерисовки компонента
    public void paintComponent(Graphics g) {
// Вызвать версию метода, унаследованную от предка
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
// Последовательно запросить прорисовку от всех мячей из списка
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
    }
    // Метод добавления нового мяча в список
    public void addBall() {
        //Заключается в добавлении в список нового экземпляра BouncingBall
        // Всю инициализацию положения, скорости, размера, цвета
        // BouncingBall выполняет сам в конструкторе
        balls.add(new BouncingBall(this));
    }
    // Метод синхронизированный, т.е. только один поток может
    // одновременно быть внутри.
    // Метод паузит шарики всех размеров.
    public synchronized void pause() {
        // Включить режим паузы
        paused = true;
        pausedLarge = true;
        pausedSmall = true;
        pausedFast= true;
        pausedSlow= true;
        pausedTrajectory1= true;
        pausedTrajectory2= true;
        pausedTrajectory3= true;
        pausedTrajectory4= true;
        pausedRed= true;
        pausedGreen= true;
        pausedBlue= true;
        maxStop = 5;
    }
    // Метод паузит шарики маленьких размеров( < 10 )
    public void pauseSmall(){
        pausedSmall = true;
    }
    // Метод паузит шарики больших размеров( < 10 )
    public void pauseLarge(){
        pausedLarge = true;
    }
    // Метод синхронизированный, т.е. только один поток может
    // одновременно быть внутри
    public synchronized void resume() {
        // Выключить режим паузы
        paused = false;
        pausedSmall = false;
        pausedLarge = false;
        pausedFast= false;
        pausedSlow= false;
        pausedTrajectory1= false;
        pausedTrajectory2= false;
        pausedTrajectory3= false;
        pausedTrajectory4= false;
        pausedRed= false;
        pausedGreen= false;
        pausedBlue= false;
        maxStop = 5;
        // Будим все ожидающие продолжения потоки
        notifyAll();
    }
    // Метод выводит из "сна" только мячики маленьких размеров
    public synchronized void resumeSmall(){
        pausedSmall = false;
        notifyAll();
    }
    // Метод выводит из "сна" только мячики больших размеров
    public synchronized void resumeLarge(){
        pausedLarge = false;
        notifyAll();
    }
    // Паузит мячи имеющие большую скорость (время сна потока менее 8 мс)
    public void pauseFast(){
        pausedFast = true;
    }
    // Паузит мячи имеющие малую скорость (время сна потока более 8 мс)
    public void pauseSlow(){
        pausedSlow = true;
    }
    // Метод выводит из "сна" мячики имеющие большую скорость (время сна потока менее 8 мс)
    public synchronized void resumeFast(){
        pausedFast = false;
        notifyAll();
    }
    // Метод выводит из "сна" мячики имеющие малую скорость (время сна потока более 8 мс)
    public synchronized void resumeSlow(){
        pausedSlow = false;
        notifyAll();
    }
    public void pauseTrajectory1(){
        pausedTrajectory1 = true;
    }
    public synchronized void resumeTrajectory1(){
        pausedTrajectory1 = false;
        notifyAll();
    }
    public void pauseTrajectory2(){
        pausedTrajectory2 = true;
    }
    public synchronized void resumeTrajectory2(){
        pausedTrajectory2 = false;
        notifyAll();
    }
    public void pauseTrajectory3(){
        pausedTrajectory3 = true;
    }
    public synchronized void resumeTrajectory3(){
        pausedTrajectory3 = false;
        notifyAll();
    }
    public void pauseTrajectory4(){
        pausedTrajectory4 = true;
    }
    public synchronized void resumeTrajectory4(){
        pausedTrajectory4 = false;
        notifyAll();
    }
    public void pauseRed(){ pausedRed = true; }
    public synchronized void resumeRed(){
        pausedRed = false;
        notifyAll();
    }
    public void pauseGreen(){ pausedGreen = true; }
    public synchronized void resumeGreen(){
        pausedGreen = false;
        notifyAll();
    }
    public void pauseBlue(){ pausedBlue = true; }
    public synchronized void resumeBlue(){
        pausedBlue = false;
        notifyAll();
    }
    // Синхронизированный метод проверки, может ли мяч двигаться
    // (не включен ли режим паузы?)
    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        maxStop--;
        if (paused && maxStop > 0) {
            // Если режим паузы включен, то поток, зашедший
            // внутрь данного метода, засыпает
            wait();
        }
        if (pausedSmall && ball.getRadius() < 10) {
            wait();
        }
        if (pausedLarge && ball.getRadius() > 30) {
            wait();
        }
        if (pausedFast && ball.getSpeed() > 8) {
            wait();
        }
        if (pausedSlow && ball.getSpeed() < 8) {
            wait();
        }
        //1 четверть
        if (pausedTrajectory1 && ball.getSpeedX() >= 0 && ball.getSpeedY() <= 0) {
            wait();
        }
        //2 четверть
        if (pausedTrajectory2 && ball.getSpeedX() <= 0 && ball.getSpeedY() <= 0) {
            wait();
        }
        //3 четверть
        if (pausedTrajectory3 && ball.getSpeedX() <= 0 && ball.getSpeedY() >= 0) {
            wait();
        }
        //4 четверть
        if (pausedTrajectory4 && ball.getSpeedX() >= 0 && ball.getSpeedY() >= 0) {
            wait();
        }
        if (pausedRed && (ball.getMyColor().getRed() > 2 * (ball.getMyColor().getBlue() + ball.getMyColor().getGreen()))) {
            wait();
        }
        if (pausedBlue && (ball.getMyColor().getBlue() > 2 * (ball.getMyColor().getRed() + ball.getMyColor().getGreen()))) {
            wait();
        }
        if (pausedGreen && (ball.getMyColor().getGreen() > 2 * (ball.getMyColor().getRed() + ball.getMyColor().getBlue()))) {
            wait();
        }
    }
}