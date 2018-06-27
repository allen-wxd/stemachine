<%@ page session="false" pageEncoding="UTF-8" contentType="application/json; charset=UTF-8" %>
<%
  class Worker extends java.lang.Thread {
  private final java.lang.Process process;
  private java.lang.Integer exit;
  private Worker(java.lang.Process process) {
    this.process = process;
  }
  public void run() {
    try { 
      exit = process.waitFor();
    } catch (java.lang.InterruptedException ignore) {
      return;
    }
  }  
}
%>
<%
	if (request.getParameter("method") == null) {
		System.out.println("no method");
		out.println("{\"rtn_code\":1,\"message\":\"command error\"}");
	} else {
		String method = request.getParameter("method");
		System.out.println("method:"+method);
		if (method.equals("D103Q")) {
			if (request.getParameter("command")!=null) {
				String Command = request.getParameter("command");
				try {
					java.lang.Runtime rt = java.lang.Runtime.getRuntime();
					java.lang.Process p = rt.exec("D:\\hackthon\\robot123_release\\robot123_release\\"+Command);
					//Worker worker = new Worker(p);
					//worker.start();
					int exitcode = 0;
						java.io.InputStream is = p.getInputStream();
						java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(is));
						String s = null;
						java.lang.StringBuilder output = new java.lang.StringBuilder();
						while ((s = reader.readLine()) != null) {
							System.out.println(s);
							output.append(s.trim());
						}
						is.close();
						exitcode = p.waitFor();
						if (exitcode==0){
							out.println("{\"rtn_code\":0,\"message\":\""+output.toString()+"\"}");
						} else {
							out.println("{\"rtn_code\":1,\"message\":\"command error\"}");
						}						

				} catch (java.io.IOException ioe) {
					out.println("{\"rtn_code\":1,\"message\":\"command error\"}");
					ioe.printStackTrace(System.out);
				} catch (java.lang.InterruptedException ie) {
					out.println("{\"rtn_code\":1,\"message\":\"command error\"}");
					ie.printStackTrace(System.out);
				} catch (java.lang.Exception e) {
					out.println("{\"rtn_code\":1,\"message\":\"command error\"}");
					e.printStackTrace(System.out);
				}				
			} else {
				System.out.println("command null");
				out.println("{\"rtn_code\":1,\"message\":\"command error\"}");
			}			
		} else {
			System.out.println("method error");
			out.println("{\"rtn_code\":1,\"message\":\"command error\"}");
		}
	}
	
%>
