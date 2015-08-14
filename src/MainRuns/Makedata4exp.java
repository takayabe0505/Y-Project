package MainRuns;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import DataModify.ExtractFile;
import IDExtract.ID_Extract_Tools;

public class Makedata4exp {

	public static final String GPSpath  = "/tmp/bousai_data/gps_";
	public static final String GPSdeeppath = "/home/c-tyabe/Data/grid/0/tmp/ktsubouc/gps_";

	public static void makedata(String outpath, HashSet<String> targetdays, HashSet<String> targetIDs) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outpath),true));

		for(String d : targetdays){
			String[] youso = d.split("-");
			String ymd = youso[0]+youso[1]+youso[2];
			String GPS = GPSpath+ymd+".tar.gz"; //ymd=yyyymmdd�̌`�ɂȂ��Ă���
			ExtractFile.extract(GPS);

			BufferedReader br = new BufferedReader(new FileReader(new File(GPSdeeppath+ymd)));
			String line = null;
			String prevline = null;
			while((line=br.readLine())!=null){
				if(ID_Extract_Tools.SameLogCheck(line,prevline)==true){
					String[] tokens = line.split("\t");
					if(tokens.length>1){
						String id = tokens[0];
						String lat = tokens[2];
						String lon = tokens[3];
						String time = converttime(tokens[4]);
						bw.write(id + "\t" + lat + "\t" + lon + "\t" + time);
						bw.newLine();
					}
					prevline = line;
				}
			}
			br.close();
			System.out.println("done " + d);
			File i = new File(GPS);
			i.delete();
		}

		bw.close();

	}
	
	public static String converttime(String t){
		String[] x = t.split("T");
		String time = x[1].substring(0,8);
		String res = x[0]+ " " + time;
		return res;
	}

}