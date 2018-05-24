# WifiClient
做为WifiServer的client端，通过socket实现与WifiServer的双工通信

一、调试方式：
与WifiServer配合使用。采用TCP协议的socket，在wifi形成的局域网内。

1、安装WifiServer到手机1，WifiServer创建WiFi热点，热点名称和密码均已设定好。

2、安装WifiClient到手机2，在手机2 setting里将wifi连接到WifiServer WifihotspotActivity创建的wifi热点。

3、手机2点击“开始传递数据”，就可以在手机1和手机2之间双向通信了。

二、代码分析
WifiClient：
 /**
     * AsyncTask which handles the communication with the server
     *
     * 双工
     */
    class ClientAsyncTask extends AsyncTask<String, Void, String> {

        //在新的工作线程中执行与Server数据交互任务
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                Log.e(TAG, "ServerAsyncTask doInBackground");
                //根据IP和端口创建socket
                Socket socket = new Socket(params[0],
                        Integer.parseInt(params[1]));

                //向Server传输数据
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);
                out.println(params[2]);

                //获取Server传输过来的数据
                InputStream is = socket.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(is));
                result = br.readLine();
                Log.e(TAG, "result : " + result);

                //关闭socket
                socket.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "ServerAsyncTask onPostExecute , s : " + s);
            tvServerMessage.setText(s);
        }
    }



