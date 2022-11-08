package Azul;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import GUI.PlayerView;
import Server.Server;
//import javafx.scene.Scene;
//import javafx.scene.web.WebView;
//import javafx.stage.Stage;

public class GUI extends JPanel {
    private JFrame menuu = new JFrame("Menu");
    Server server = null;
    PlayerView playerView;
    // Dimension scrensize = Toolkit.getDefaultToolkit().getScreenSize();

    WindowAdapter windowAdapter = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            if(server!=null) server.killServer();
            super.windowClosing(e);
        }
    };

    public GUI() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        JLabel menu = new JLabel(new ImageIcon("src/img/background.png"));

        JLabel hill = new JLabel(new ImageIcon("src/img/hill2.png"));
        hill.setOpaque(false);
        hill.setBounds(453, 518, 1103, 320);

        JLabel own = new JLabel(new ImageIcon("src/img/own.gif"));
        own.setOpaque(false);
        own.setBounds(770, 538, 100, 110);


        //######################################  MUZYKA  #############################################################################


        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/img/sound.wav").getAbsoluteFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        clip.loop(5);

        //Zmiana poziomu dżwięku
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = 0.5;       // set the gain (between 0.0 and 1.0)
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);


        //######################################  ANIMACJA NA GLÓWNEJ STRONIE  #########################################################
        JLabel azul = new JLabel(new ImageIcon("src/img/azul.png"));
        JLabel zad = new JLabel(new ImageIcon("src/img/zad_obl.png"));
        JLabel per1 = new JLabel(new ImageIcon("src/img/peredn.png"));
        JLabel per2 = new JLabel(new ImageIcon("src/img/peredn.png"));


        int x = 20; //КОРДИНАТЫ
        final int[] y = {50};

        final int[] zadX = {-35};
        int zadY = 500;

        final int[] per1X = {-384};
        int per1Y = 600;
        final int[] per2X = {-2304};
        int per2Y = 600;


        azul.setOpaque(false);
        azul.setSize(700, 384);
        azul.setBounds(x, y[0], azul.getWidth(), azul.getHeight());

        zad.setOpaque(false);
        zad.setSize(1890, 349);
        zad.setBounds(zadX[0], zadY, zad.getWidth(), zad.getHeight());

        per1.setOpaque(false);
        per1.setSize(1920, 246);
        per1.setBounds(per1X[0], per1Y, per1.getWidth(), per1.getHeight());
        per2.setOpaque(false);
        per2.setSize(1920, 246);
        per2.setBounds(per2X[0], per2Y, per2.getWidth(), per2.getHeight());

        final int[] count = {0};

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                azul.setBounds(x, y[0], azul.getWidth(), azul.getHeight()); //УСТАНАВЛИВАЕМ КОРДИНАТЫ НАЧАЛЬНЫЕ
                zad.setBounds(zadX[0], zadY, zad.getWidth(), zad.getHeight());
                per1.setBounds(per1X[0], per1Y, per1.getWidth(), per1.getHeight());
                per2.setBounds(per2X[0], per2Y, per2.getWidth(), per2.getHeight());

                if (count[0] >= 0) {
                    y[0]++;
                    count[0]++;
                } else if (count[0] < 0) {
                    y[0]--;
                    count[0]++;
                }
                if (count[0] == 40) {
                    y[0]--;
                    count[0] = -40;
                }

                zadX[0]--;
                if (zadX[0] == -330) {
                    zadX[0] = -35;
                }

                per1X[0]++;
                per2X[0]++;
                if (per1X[0] == 1536) {
                    per1X[0] = -2304;
                }
                if (per2X[0] == 1536) {
                    per1X[0] = -2304;
                }

            }
        };
        new java.util.Timer().schedule(task, 0, 85);

        //##############################################################################################################################

        //PRZYCISK I STRONA PLAY
        JButton play = new JButton(new ImageIcon("src/img/play.png"));
        play.setBounds(690, 100, 248, 73);
        play.setOpaque(false);
        play.setContentAreaFilled(false);
        //play.setBorderPainted(false);
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame choose = new JFrame("Game options");                               //nadpis na krawędzi
                JLabel background = new JLabel(new ImageIcon("src/img/settings_fon.png"));

                JLabel enter = new JLabel(new ImageIcon("src/img/enter your name_.png"));
                enter.setOpaque(false);
                enter.setBounds(50, 480, 649, 67);
                enter.setVisible(false);

                JTextField name = new JTextField();
                name.setBounds(720, 480, 292, 65);
                name.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String yourName = name.getText();
                    }
                });
                name.setVisible(false);

                JLabel firstPlayer = new JLabel(new ImageIcon("src/img/1st player.png"));
                firstPlayer.setOpaque(false);
                firstPlayer.setBounds(100, 490, 343, 48);
                firstPlayer.setVisible(false);

                JTextField first = new JTextField();
                first.setBounds(480, 480, 150, 64);
                first.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String firsttPlayer = first.getText();
                    }
                });
                first.setVisible(false);

                JLabel secondPlayer = new JLabel(new ImageIcon("src/img/2nd player.png"));
                secondPlayer.setOpaque(false);
                secondPlayer.setBounds(700, 490, 349, 48);
                secondPlayer.setVisible(false);

                JTextField second = new JTextField();
                second.setBounds(1100, 480, 150, 64);
                second.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String seconddPlayer = second.getText();
                    }
                });
                second.setVisible(false);

                JLabel thirdPlayer = new JLabel(new ImageIcon("src/img/3rd player.png"));
                thirdPlayer.setOpaque(false);
                thirdPlayer.setBounds(100, 590, 343, 48);
                thirdPlayer.setVisible(false);

                JTextField third = new JTextField();
                third.setBounds(480, 580, 150, 64);
                third.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String thirddPlayer = third.getText();
                    }
                });
                third.setVisible(false);

                JLabel fourthPlayer = new JLabel(new ImageIcon("src/img/4th player.png"));
                fourthPlayer.setOpaque(false);
                fourthPlayer.setBounds(700, 590, 349, 48);
                fourthPlayer.setVisible(false);

                JTextField fourth = new JTextField();
                fourth.setBounds(1100, 580, 150, 64);
                fourth.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String fourthhPlayer = fourth.getText();
                    }
                });
                fourth.setVisible(false);



                JLabel select = new JLabel(new ImageIcon("src/img/Select game options.png"));
                select.setOpaque(false);
                select.setBounds(60, 20, 835, 261);

                JLabel tree = new JLabel(new ImageIcon("src/img/tree.png"));
                tree.setOpaque(false);
                tree.setBounds(210, -70, 1347, 929);


                JLabel players = new JLabel(new ImageIcon("src/img/players_.png"));
                players.setOpaque(false);
                players.setBounds(50, 387, 329, 66);

                JLabel twoPlayers = new JLabel(new ImageIcon("src/img/2.png"));
                twoPlayers.setOpaque(false);
                twoPlayers.setBounds(500, 400, 36, 49);

                JLabel threePlayers = new JLabel(new ImageIcon("src/img/3.png"));
                threePlayers.setOpaque(false);
                threePlayers.setBounds(670, 400, 37, 51);

                JLabel fourPlayers = new JLabel(new ImageIcon("src/img/4.png"));
                fourPlayers.setOpaque(false);
                fourPlayers.setBounds(830, 400, 40, 48);



                JLabel gameONLINE = new JLabel(new ImageIcon("src/img/online.png"));
                gameONLINE.setOpaque(false);
                gameONLINE.setBounds(130, 300, 238, 49);

                JLabel gameOFFLINE = new JLabel(new ImageIcon("src/img/offline.png"));
                gameOFFLINE.setOpaque(false);
                gameOFFLINE.setBounds(570, 300, 274, 50);



                JLabel portLabel = new JLabel(new ImageIcon("src/img/port.png"));
                portLabel.setOpaque(false);
                portLabel.setBounds(100, 690, 161, 46);
                portLabel.setVisible(false);

                JLabel ipLabel = new JLabel(new ImageIcon("src/img/ip.png"));
                ipLabel.setOpaque(false);
                ipLabel.setBounds(710, 690, 58, 41);
                ipLabel.setVisible(false);

                JTextField port = new JTextField();
                port.setBounds(290, 680, 292, 65);
                port.setVisible(false);
                port.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String yourPort = port.getText();
                    }
                });
                JTextField ip = new JTextField();
                ip.setBounds(800, 680, 292, 65);
                ip.setVisible(false);
                ip.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        String yourIp = ip.getText();
                    }
                });

                JLabel gameHOST = new JLabel(new ImageIcon("src/img/host_a_game.png"));
                gameHOST.setOpaque(false);
                gameHOST.setBounds(80, 600, 504, 49);
                gameHOST.setVisible(false);

                JLabel gameJOIN = new JLabel(new ImageIcon("src/img/join_the_game.png"));
                gameJOIN.setOpaque(false);
                gameJOIN.setBounds(660, 600, 568, 49);
                gameJOIN.setVisible(false);

                ButtonGroup game = new ButtonGroup();
                JRadioButton host = new JRadioButton("", true);
                host.setOpaque(false);
                host.setContentAreaFilled(false);
                host.setBounds(50, 600, 519, 50);
                game.add(host);
                host.setVisible(false);
                host.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        portLabel.setVisible(true);
                        port.setVisible(true);
                        ipLabel.setVisible(false);
                        ip.setVisible(false);
                    }
                });

                JRadioButton join = new JRadioButton("", false);
                join.setOpaque(false);
                join.setContentAreaFilled(false);
                join.setBounds(640, 600, 584, 50);
                game.add(join);
                join.setVisible(false);
                join.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        portLabel.setVisible(true);
                        port.setVisible(true);
                        ipLabel.setVisible(true);
                        ip.setVisible(true);
                    }
                });

                ButtonGroup offon = new ButtonGroup();
                JRadioButton online = new JRadioButton("");
                online.setOpaque(false);
                online.setContentAreaFilled(false);
                online.setBounds(100, 300, 268, 49);
                offon.add(online);
                online.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        gameHOST.setVisible(true);
                        gameJOIN.setVisible(true);
                        host.setVisible(true);
                        join.setVisible(true);
                        name.setVisible(true);
                        enter.setVisible(true);

                        port.setVisible(true);
                        portLabel.setVisible(true);

                        firstPlayer.setVisible(false);
                        first.setVisible(false);
                        secondPlayer.setVisible(false);
                        second.setVisible(false);
                        thirdPlayer.setVisible(false);
                        third.setVisible(false);
                        fourthPlayer.setVisible(false);
                        fourth.setVisible(false);

                    }
                });

                JRadioButton offline = new JRadioButton("");
                offline.setOpaque(false);
                offline.setSelected(true);
                offline.setContentAreaFilled(false);
                offline.setBounds(540, 300, 304, 50);
                offon.add(offline);
                offline.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        portLabel.setVisible(false);
                        port.setVisible(false);
                        ipLabel.setVisible(false);
                        ip.setVisible(false);

                        enter.setVisible(false);
                        name.setVisible(false);

                        gameHOST.setVisible(false);
                        gameJOIN.setVisible(false);
                        host.setVisible(false);
                        join.setVisible(false);
                    }
                });

                ButtonGroup player = new ButtonGroup();
                JRadioButton two = new JRadioButton("");
                two.setName("two");
                two.setSelected(true);
                two.setOpaque(false);
                two.setContentAreaFilled(false);
                two.setBounds(470, 400, 66, 50);
                player.add(two);
                two.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (offline.isSelected()) {
                            firstPlayer.setVisible(true);
                            first.setVisible(true);

                            secondPlayer.setVisible(true);
                            second.setVisible(true);

                            thirdPlayer.setVisible(false);
                            third.setVisible(false);

                            fourthPlayer.setVisible(false);
                            fourth.setVisible(false);
                        }
                    }
                });

                JRadioButton three = new JRadioButton("");
                two.setName("three");
                three.setOpaque(false);
                three.setContentAreaFilled(false);
                three.setBounds(640, 400, 67, 50);
                player.add(three);
                three.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (offline.isSelected()) {
                            firstPlayer.setVisible(true);
                            first.setVisible(true);

                            secondPlayer.setVisible(true);
                            second.setVisible(true);

                            thirdPlayer.setVisible(true);
                            third.setVisible(true);

                            fourthPlayer.setVisible(false);
                            fourth.setVisible(false);
                        }
                    }
                });


                JRadioButton four = new JRadioButton("");
                two.setName("four");
                four.setOpaque(false);
                four.setContentAreaFilled(false);
                four.setBounds(800, 400, 70, 50);
                player.add(four);
                four.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if (offline.isSelected()) {
                            firstPlayer.setVisible(true);
                            first.setVisible(true);

                            secondPlayer.setVisible(true);
                            second.setVisible(true);

                            thirdPlayer.setVisible(true);
                            third.setVisible(true);

                            fourthPlayer.setVisible(true);
                            fourth.setVisible(true);
                        }
                    }
                });


                JLabel zadneeOBL = new JLabel(new ImageIcon("src/img/zad_obl.png"));
                JLabel peredneeOBL = new JLabel(new ImageIcon("src/img/peredn.png"));

                final int[] zadnx = {-35};
                int zadny = 500;
                final int[] perdX = {-380};
                int perdY = 600;

                zadneeOBL.setOpaque(false);
                zadneeOBL.setSize(1890, 349);
                zadneeOBL.setBounds(zadnx[0], zadny, zadneeOBL.getWidth(), zadneeOBL.getHeight());

                peredneeOBL.setOpaque(false);
                peredneeOBL.setSize(1920, 246);
                peredneeOBL.setBounds(perdX[0], perdY, peredneeOBL.getWidth(), peredneeOBL.getHeight());


                TimerTask taskkk = new TimerTask() {
                    @Override
                    public void run() {
                        zadneeOBL.setBounds(zadnx[0], zadny, zadneeOBL.getWidth(), zadneeOBL.getHeight());
                        peredneeOBL.setBounds(perdX[0], perdY, peredneeOBL.getWidth(), peredneeOBL.getHeight());

                        zadnx[0]--;
                        if (zadnx[0] == -330) {
                            zadnx[0] = -35;
                        }

                        perdX[0]++;
                        if (perdX[0] == 0) {
                            perdX[0] = -380;
                        }
                    }
                };
                new Timer().schedule(taskkk, 0, 85);


                JButton play = new JButton(new ImageIcon("src/img/play.png"));
                play.setOpaque(false);
                //play.setContentAreaFilled(false);
                play.setBounds(1250, 760, 292, 65);
                play.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {

                        int numberOfPlayers = 0;
                        if (two.isSelected()) { numberOfPlayers = 2;}
                        else if (three.isSelected()){ numberOfPlayers = 3;}
                        else if (four.isSelected()){ numberOfPlayers = 4;}

                        JFrame game = new JFrame("Game");
                        JLabel background = new JLabel(new ImageIcon("src/img/game.png"));

                        boolean singleplayer = offline.isSelected();

                        if(singleplayer){               /// singleplayer
                            server = new Server(numberOfPlayers, 6666, true);
                            Thread serverThread = new Thread(server);
                            serverThread.start();
                            playerView = new PlayerView(numberOfPlayers, singleplayer,"localhost",6666);
                        }else if(join.isSelected()){    //// join multi
                            playerView = new PlayerView(numberOfPlayers, singleplayer, ip.getText(),Integer.parseInt(port.getText()));
                        } else {    //// host multi
                            server = new Server(numberOfPlayers, Integer.parseInt(port.getText()), false);
                            Thread serverThread = new Thread(server);
                            serverThread.start();
                            playerView = new PlayerView(numberOfPlayers, singleplayer, "localhost",Integer.parseInt(port.getText()));
                        }


                        JLabel azul = new JLabel(new ImageIcon("src/img/azul2.png"));
                        int x = 170;
                        final int[] y = {250};
                        azul.setOpaque(false);
                        azul.setSize(510, 281);
                        azul.setBounds(x, y[0], azul.getWidth(), azul.getHeight());

                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                azul.setBounds(x, y[0], azul.getWidth(), azul.getHeight()); //УСТАНАВЛИВАЕМ КОРДИНАТЫ НАЧАЛЬНЫЕ

                                if (count[0] >= 0) {
                                    y[0]++;
                                    count[0]++;
                                } else if (count[0] < 0) {
                                    y[0]--;
                                    count[0]++;
                                }
                                if (count[0] == 40) {
                                    y[0]--;
                                    count[0] = -40;
                                }
                            }
                        };
                        new java.util.Timer().schedule(task, 0, 85);


                        JLabel hill = new JLabel(new ImageIcon("src/img/hill1.png"));
                        hill.setOpaque(false);
                        hill.setBounds(0, 565, 898, 261);

                        JLabel zadneeOBL = new JLabel(new ImageIcon("src/img/zad_obl.png"));
                        JLabel peredneeOBL = new JLabel(new ImageIcon("src/img/peredn.png"));

                        final int[] zadnx = {-35};
                        int zadny = 500;
                        final int[] perdX = {-380};
                        int perdY = 600;

                        zadneeOBL.setOpaque(false);
                        zadneeOBL.setSize(1890, 349);
                        zadneeOBL.setBounds(zadnx[0], zadny, zadneeOBL.getWidth(), zadneeOBL.getHeight());

                        peredneeOBL.setOpaque(false);
                        peredneeOBL.setSize(1920, 246);
                        peredneeOBL.setBounds(perdX[0], perdY, peredneeOBL.getWidth(), peredneeOBL.getHeight());


                        TimerTask taskkkkk = new TimerTask() {
                            @Override
                            public void run() {
                                zadneeOBL.setBounds(zadnx[0], zadny, zadneeOBL.getWidth(), zadneeOBL.getHeight());
                                peredneeOBL.setBounds(perdX[0], perdY, peredneeOBL.getWidth(), peredneeOBL.getHeight());

                                zadnx[0]--;
                                if (zadnx[0] == -330) {
                                    zadnx[0] = -35;
                                }

                                perdX[0]++;
                                if (perdX[0] == 0) {
                                    perdX[0] = -380;
                                }
                            }
                        };
                        new Timer().schedule(taskkkkk, 0, 85);

                        JButton back = new JButton(new ImageIcon("src/img/back.png"));
                        back.setOpaque(false);
                        back.setContentAreaFilled(false);
                        back.setBounds(0, 760, 292, 65);
                        back.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent actionEvent) {
                                game.setVisible(false);
                                if(server!=null) server.killServer();
                                menuu.setVisible(true);
                            }
                        });


                        game.add(playerView);
                        game.add(azul);
                        game.add(back);
                        game.add(peredneeOBL);
                        game.add(hill);
                        game.add(zadneeOBL);
                        game.add(background);

                        game.pack();
                        game.setSize(1536, 864);
                        game.setLayout(null);
                        game.setVisible(true);
                        game.setResizable(false);
                        game.addWindowListener(windowAdapter);
                        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                        menuu.setVisible(false);
                        choose.setVisible(false);
                    }
                });

                JButton back = new JButton(new ImageIcon("src/img/back.png"));
                back.setOpaque(false);
                back.setContentAreaFilled(false);
                back.setBounds(0, 760, 292, 65);
                back.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        choose.setVisible(false);
                        menuu.setVisible(true);
                    }
                });

                choose.add(play);
                choose.add(back);

                choose.add(name);
                choose.add(online);
                choose.add(offline);

//                choose.add(first);
//                choose.add(firstPlayer);
//                choose.add(second);
//                choose.add(secondPlayer);
//                choose.add(third);
//                choose.add(thirdPlayer);
//                choose.add(fourth);
//                choose.add(fourthPlayer);

                choose.add(gameHOST);
                choose.add(gameJOIN);
                choose.add(host);
                choose.add(join);

                choose.add(portLabel);
                choose.add(ipLabel);
                choose.add(port);
                choose.add(ip);

                choose.add(enter);
                choose.add(players);

                choose.add(gameONLINE);
                choose.add(gameOFFLINE);

                choose.add(twoPlayers);
                choose.add(threePlayers);
                choose.add(fourPlayers);

                choose.add(two);
                choose.add(three);
                choose.add(four);


                choose.add(select);
                choose.add(tree);
                choose.add(peredneeOBL);
                choose.add(zadneeOBL);
                choose.add(background);

                choose.pack();
                choose.setSize(1536, 864);
                choose.setLayout(null);
                choose.setVisible(true);
                choose.setResizable(false);
                choose.addWindowListener(windowAdapter);
                choose.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                menuu.setVisible(false);
            }
        });


        //PRZYCISK I STRONA SETTINGS
        JButton settings = new JButton(new ImageIcon("src/img/settings.png"));
        settings.setBounds(850, 200, 436, 60);
        settings.setOpaque(false);
        settings.setContentAreaFilled(false);
        settings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame set = new JFrame("Settings");
                JLabel background = new JLabel(new ImageIcon("src/img/settings_fon.png"));


                JLabel nagl = new JLabel(new ImageIcon("src/img/settings_big.png"));
                nagl.setOpaque(false);
                nagl.setBounds(130, 5, 835, 261);

                JLabel tree = new JLabel(new ImageIcon("src/img/tree.png"));
                tree.setOpaque(false);
                tree.setBounds(210, -70, 1347, 929);
                tree.setDoubleBuffered(true);


                JLabel sound = new JLabel(new ImageIcon("src/img/Sounds.png"));
                sound.setOpaque(false);
                sound.setBounds(105, 305, 291, 52);

                JLabel musicc = new JLabel(new ImageIcon("src/img/MUsic.png"));
                musicc.setOpaque(false);
                musicc.setBounds(140, 410, 228, 52);

                JLabel language = new JLabel(new ImageIcon("src/img/Language.png"));
                language.setOpaque(false);
                language.setBounds(50, 515, 435, 52);

///////////////Wlączanie/ wyłączanie dzwięków
                JLabel on = new JLabel(new ImageIcon("src/img/on.png"));
                on.setOpaque(false);
                on.setBounds(620, 310, 79, 43);

                JLabel off = new JLabel(new ImageIcon("src/img/of.png"));
                off.setOpaque(false);
                off.setBounds(740, 310, 116, 43);

                ButtonGroup soundOffOn = new ButtonGroup();
                JRadioButton soundON = new JRadioButton("", true);
                soundON.setOpaque(false);
                soundON.setContentAreaFilled(false);
                soundON.setBounds(600, 310, 79, 43);

                JRadioButton soundOF = new JRadioButton("");
                soundOF.setOpaque(false);
                soundOF.setContentAreaFilled(false);
                soundOF.setBounds(720, 310, 116, 43);

                soundOffOn.add(soundON);
                soundOffOn.add(soundOF);

//////////  Wlączanie/ wyłączanie muzyki
                JLabel onM = new JLabel(new ImageIcon("src/img/on.png"));
                onM.setOpaque(false);
                onM.setBounds(620, 420, 79, 43);

                JLabel offM = new JLabel(new ImageIcon("src/img/of.png"));
                offM.setOpaque(false);
                offM.setBounds(740, 420, 116, 43);

                ButtonGroup musicOffOn = new ButtonGroup();
                JRadioButton musicON = new JRadioButton("", true);
                musicON.setOpaque(false);
                musicON.setContentAreaFilled(false);
                musicON.setBounds(600, 420, 79, 43);
                musicON.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        clip.start();
                    }
                });

                JRadioButton musicOF = new JRadioButton("");
                musicOF.setOpaque(false);
                musicOF.setContentAreaFilled(false);
                musicOF.setBounds(720, 420, 116, 43);
                musicOF.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        clip.stop();
                    }
                });

                musicOffOn.add(musicON);
                musicOffOn.add(musicOF);
/////////////


                JButton back = new JButton(new ImageIcon("src/img/back.png"));
                back.setOpaque(false);
                back.setContentAreaFilled(false);
                back.setBounds(0, 770, 237, 57);
                back.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        set.setVisible(false);
                        menuu.setVisible(true);
                    }
                });

                JLabel zad = new JLabel(new ImageIcon("src/img/zad_obl.png"));
                JLabel per = new JLabel(new ImageIcon("src/img/peredn.png"));

                final int[] zadxx = {-35};
                int zadyy = 500;
                final int[] perX = {-380};
                int perY = 600;

                zad.setOpaque(false);
                zad.setSize(1890, 349);
                zad.setBounds(zadxx[0], zadyy, zad.getWidth(), zad.getHeight());

                per.setOpaque(false);
                per.setSize(1920, 246);
                per.setBounds(perX[0], perY, per.getWidth(), per.getHeight());


                TimerTask taskk = new TimerTask() {
                    @Override
                    public void run() {
                        zad.setBounds(zadxx[0], zadyy, zad.getWidth(), zad.getHeight());
                        per.setBounds(perX[0], perY, per.getWidth(), per.getHeight());

                        zadxx[0]--;
                        if (zadxx[0] == -330) {
                            zadxx[0] = -35;
                        }

                        perX[0]++;
                        if (perX[0] == 0) {
                            perX[0] = -380;
                        }
                    }
                };
                new java.util.Timer().schedule(taskk, 0, 85);

//                set.add(soundON);
//                set.add(soundOF);
//                set.add(on);
//                set.add(off);

                set.add(musicON);
                set.add(musicOF);
                set.add(onM);
                set.add(offM);

                //set.add(sound);
                set.add(musicc);
               // set.add(language);
                set.add(back);

                set.add(tree);
                set.add(per);
                set.add(zad);
                set.add(nagl);
                set.add(background);


                set.pack();
                set.setSize(1536, 864);
                set.setLayout(null);
                set.setVisible(true);
                set.setResizable(false);
                set.addWindowListener(windowAdapter);
                set.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                menuu.setVisible(false);
            }
        });

        //PRZYCISK I STRONA RULES
        JButton rules = new JButton(new ImageIcon("src/img/rules.png"));
        rules.setBounds(1137, 300, 283, 66);
        rules.setOpaque(false);
        rules.setContentAreaFilled(false);
        rules.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame rules = new JFrame("Rules");                               //nadpis na krawędzi
                JLabel background = new JLabel(new ImageIcon("src/img/game_options.png"));

                JButton back = new JButton(new ImageIcon("src/img/back.png"));
                back.setBounds(0, 735, 292, 65);
                back.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        rules.setVisible(false);
                        menuu.setVisible(true);
                    }
                });


//                WebView webview = new WebView();
//                webview.getEngine().load(
//                        "https://www.youtube.com/watch?v=byHjDCxF5WU"
//                );
//                webview.setPrefSize(640, 390);
//
//                Stage stage = new Stage( );
//                stage.setScene(new Scene(webview));
//                stage.show();


                //rules.add(video);

                rules.setResizable(false);
                rules.setSize(800, 800);
                rules.add(background);
                rules.pack();
                rules.add(back);
                rules.setLayout(null);
                rules.setVisible(true);
                rules.addWindowListener(windowAdapter);
                rules.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                menuu.setVisible(false);
            }
        });


        //PRZYCISK QUIT
        ImageIcon quitt = new ImageIcon("src/img/quit.png");
        JButton quit = new JButton(quitt);
        quit.setBounds(1306, 400, 201, 67);
        quit.setOpaque(false);
        quit.setContentAreaFilled(false);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        menuu.add(play);         // wyższa warstwa
        menuu.add(rules);
        menuu.add(settings);
        menuu.add(quit);

        menuu.add(own);
        menuu.add(per1);
        menuu.add(per2);
        menuu.add(hill);
        menuu.add(zad);
        menuu.add(azul);
        menuu.add(menu);         // niższa warstwa

        menuu.pack();
        menuu.setSize(1536, 864);
        menuu.setLayout(null);
        menuu.setVisible(true);
        menuu.setResizable(false);
        menuu.addWindowListener(windowAdapter);
        menuu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        GUI intro = new GUI();
    }
}