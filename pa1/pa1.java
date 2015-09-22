package pa1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;

public class pa1 {
	
	public static String header = "";
	//public static String faces = "";
	public static Vector<String> faces = new Vector<String>();
	public static int num_vertices = 0;
	public static int num_faces = 0;
	public static Matrix points;
	public static Matrix scalar_matrix = null;
	public static Matrix transformation_matrix = null;
	public static Matrix rotation_matrix = null;
	public static Matrix og_trans = null;
	public static Matrix og_trans_i = null;
	
	public static void do_input(String inputfile) throws IOException {
				float[][] vertices = null;

	            Scanner scan = new Scanner(new File(inputfile));
	            boolean part_of_header = true;
	            int counter = 0;
	            float[] min_values = {Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE};
	            float[] max_values = {Float.MIN_NORMAL, Float.MIN_NORMAL, Float.MIN_NORMAL};//new float[3];
	            float[] sum_values = {0f, 0f, 0f};
	            while(scan.hasNextLine()) {
	                String line = scan.nextLine();
	                //System.out.println(line);
	                String line_parts[] = line.split(" ");
	                if(part_of_header) {
	                    if(line.contains("element vertex")) {
	                        num_vertices = Integer.parseInt(line_parts[2]);
	                        vertices = new float[4][num_vertices];
	                        //counter = num_vertices;
	                    } else if(line.contains("element face")) {
	                        num_faces = Integer.parseInt(line_parts[2]);
	                        //faces = new int[num_faces][]
	                    } else if(line.contains("end_header")) {
	                        part_of_header = false;
	                        //header += line;
	                        //continue;
	                    } else if(line.contains("property float")) {
	                        //System.out.println(line_parts[2].charAt(0));
	                        if((line_parts[2].charAt(0) != 'x') && (line_parts[2].charAt(0) != 'y') && (line_parts[2].charAt(0) != 'z')) {
	                            continue;
	                        }
	                    }
	                    
	                    header += line + "\n";
	                    //if(!part_of_header)
	                       // break;
	                    
	                    
	                } else { 
	                    if(counter < num_vertices) {
	                       for(int i = 0; i < 4; i++) {
	                    	   float f = 0f;
	                    	   if(i != 3) {
	                    		   f = Float.parseFloat(line_parts[i]);//vertices[i][counter];
	                    	   }
	                    	   switch(i) {
	                    	   		case 0:
	                    	   		case 1:
	                    	   		case 2:
	                    	   			if(f < min_values[i]) {
	                    	   				min_values[i] = f;
	                    	   			}
	                    	   			if(f > max_values[i]) {
	                    	   				max_values[i] = f;
	                    	   			}
	                    	   			sum_values[i] += f;
	                    	   			vertices[i][counter] = f;
	                    	   			break;
	                    	   		case 3:
	                    	   			vertices[i][counter] = 1f;
	                    	   			
	                    	   }
	                    	   /*if(i == 3) {
	                        		vertices[i][counter] = 1f;
	                        		continue;
	                        	}
	                    	   
	                        	vertices[i][counter] = Float.parseFloat(line_parts[i]); */
	                        	
	                        }
	                        counter++;
	                    } else {
	                    	//faces += line + "\n";
	                    	faces.add(line);
	                    }
	                    
	                }
	                
	            
	            }
	         
	            float[] avg_values = new float[3];
	            for(int av = 0; av < 3; av++) {
	            	avg_values[av] = sum_values[av]/num_vertices;
	            }
	            
	            float[][] ot = {{1f, 0f, 0f, -avg_values[0]}, {0f, 1f, 0f, -avg_values[1]}, {0f, 0f, 1f, -avg_values[2]}, {0f, 0f, 0f, 1f}};
	    		og_trans = new Matrix(ot);
	    		og_trans_i = get_transformation_inverse(og_trans);
	    		//System.out.println("og_trans:\n" + og_trans);
	    		//System.out.println("og_trans_i:\n" + og_trans_i);
	    		
	            
	            System.out.println("Model: " + inputfile + " loaded.");
	            System.out.println("Number of vertices: " + num_vertices);
	            System.out.println("Number of faces: " + num_faces);
	            System.out.println("Mean vertex of model: (" + avg_values[0] + ", " + avg_values[1] + ", " + avg_values[2] + ")");
	            System.out.println("Bounding box x-range: [" + min_values[0] + ", " + max_values[0] + "]");
	            System.out.println("Bounding box y-range: [" + min_values[1] + ", " + max_values[1] + "]");
	            System.out.println("Bounding box z-range: [" + min_values[2] + ", " + max_values[2] + "]");
	            points = new Matrix(vertices);
	           // System.out.println(points);
	            //System.out.println("Header:\n" + header);
	            //System.out.println("vertices:\n" + vertices[0][4]);
	            //System.out.println("faces:\n" + faces);
	            
	            scan.close();
	}
	
	public static void do_output(String outputfile) throws IOException {
		PrintWriter out = new PrintWriter(outputfile);
		out.write(header);
		for(int i = 0; i < points.size_col; i++) {
			for(int j  = 0; j < 3; j++) {
				out.write(points.matrix[j][i] + " ");
			}
			out.write("\n");
		}
		System.out.println("size: " + faces.size());
		for(int f = 0; f < faces.size(); f++) {
			out.write(faces.get(f)+"\n");
		}
		//out.write(faces.get(0));
		//out.write(faces);
		out.close();
	}
	
	public static void get_user_io() {
		Scanner keyboard = new Scanner(System.in);
		while(true) {
			System.out.print("Enter command: ");
			String input = keyboard.nextLine();
			String[] input_parts = input.split(" ");
			float x = 0, y = 0, z = 0, theta = 0;
			if(input_parts.length >= 4) {
				x = Float.parseFloat(input_parts[1]);
				y = Float.parseFloat(input_parts[2]);
				z = Float.parseFloat(input_parts[3]);
				if(input_parts.length == 5) {
					theta = Float.parseFloat(input_parts[4]);
				}
			} else {
				if(input_parts[0].charAt(0) != 'W' && input_parts.length != 1) {
					System.out.println("Invalid number of arguments for command.");
					continue;
				}
			}
			switch(input_parts[0].charAt(0)) {
				case 'S':
					scale(x, y, z);
					continue;
				case 'T':
					translate(x, y, z);
					continue;
				case 'R':
					rotate(x, y, z, theta);
					continue;
				case 'W':
					finish();
					break;
				default:
					System.out.println("Usage\nS sx sy sz\nT tx ty tz\nR rx ry rz theta\nW");
					
			}
			break;
		}
		keyboard.close();
		
	}
	
	public static void scale(float sx, float sy, float sz) {
		//System.out.println("Scale");
		float[][] s = {{sx, 0f, 0f, 0f}, {0f, sy, 0f, 0f}, {0f, 0f, sz, 0f}, {0f, 0f, 0f, 1}};
		Matrix new_scalar = Matrix.matrix_multiplication(og_trans_i, new Matrix(s));
		new_scalar = Matrix.matrix_multiplication(new_scalar, og_trans);
		if(scalar_matrix == null) {
			scalar_matrix = new Matrix(new_scalar);
		} else {
			scalar_matrix = Matrix.matrix_multiplication(scalar_matrix, new_scalar);
		}
		
		/*if(scalar_matrix == null) {
			scalar_matrix = new Matrix(s);
		} else {
			scalar_matrix = Matrix.matrix_multiplication(scalar_matrix, new Matrix(s));
		}*/
	}
	
	public static void translate(float tx, float ty, float tz) {
		//System.out.println("Translate");
		float[][] t = {{1f, 0f, 0f, tx}, {0f, 1f, 0f, ty}, {0f, 0f, 1f, tz}, {0f, 0f, 0f, 1f}};
		if(transformation_matrix == null) {
			transformation_matrix = new Matrix(t);
		} else {
			transformation_matrix = Matrix.matrix_multiplication(transformation_matrix, new Matrix(t));
		}
	}

	public static void rotate(float rx, float ry, float rz, float theta) {
		//System.out.println("Rotate");
		if(rx == 0 && ry == 0 && rz == 0) {
			//System.out.println("Invalid rotation values: " + rx + ", " + ry + ", " + rz);
			return;
		}
		float[] vec = {rx, ry, rz, 0};
		float[] w = normalize(vec);
		//System.out.println("W normalized: " + w[0] + ", " + w[1] + ", " + w[2] + ", " + w[3]);
		float min = w[0];
		int min_pos = 0;
		for(int i = 0; i < 3; i++) {
			if(w[i] < min) {
				min = w[i];
				min_pos = i;
				//System.out.println("Min: " + min + ", at pos: " + min_pos);
			}
		}
		float[] m = new float[4];
		for(int i = 0; i < 4; i++) {
			if(i == min_pos) {
				m[i] = 1;
			} else {
				m[i] = w[i];
			}
		}
		m = normalize(m);
		//System.out.println("m normalized: " + m[0] + ", " + m[1] + ", " + m[2] + ", " + m[3]);
		
		float[] u = cross_product(w, m);
		u = normalize(u);
		//System.out.println("u normalized: " + u[0] + ", " + u[1] + ", " + u[2] + ", " + u[3]);
		float[] v = cross_product(w, u);
		v = normalize(v);
		//System.out.println("v normalized: " + v[0] + ", " + v[1] + ", " + v[2] + ", " + v[3]);
		float[][] Rw = new float[4][4];
		/*for(int r = 0; r < 4; r++) {
			for(int c = 0; c < 4; c++) {
				if(r == 0) {
					Rw = 
				}
				Rw[r][c] = 
			}
		}*/
		Rw[0][0] = u[0]; Rw[0][1] = u[1]; Rw[0][2] = u[2]; Rw[0][3] = 0;
		Rw[1][0] = v[0]; Rw[1][1] = v[1]; Rw[1][2] = v[2]; Rw[1][3] = 0;
		Rw[2][0] = w[0]; Rw[2][1] = w[1]; Rw[2][2] = w[2]; Rw[2][3] = 0;
		Rw[3][0] = 0; 	 Rw[3][1] = 0;	  Rw[3][2] = 0;	   Rw[3][3] = 1;
		
		Matrix RW = new Matrix(Rw);
		
		float[][] Rz = new float[4][4];
		Rz[0][0] = (float) Math.cos(get_radians(theta)); Rz[0][1] = (float) -Math.sin(get_radians(theta)); Rz[0][2] = 0; Rz[0][3] = 0;
		Rz[1][0] = (float) Math.sin(get_radians(theta)); Rz[1][1] = (float) Math.cos(get_radians(theta)); Rz[1][2] = 0; Rz[1][3] = 0;
		Rz[2][0] = 0; Rz[2][1] = 0;  Rz[2][2] = 1; Rz[2][3] = 0;
		Rz[3][0] = 0; Rz[3][1] = 0;  Rz[3][2] = 0; Rz[3][3] = 1;
		
		Matrix RZ = new Matrix(Rz);
		Matrix RWt = null;
		RWt = Matrix.get_transpose(RW);
		/*if(RW.is_orthonormal()) {
			RWt = Matrix.get_transpose(RW);
		} else {
			System.out.println("Not an orthonormal matrix:");
			System.out.println(RW);
			System.exit(-1);
		}*/
		
		/***********Matrix new_scalar = Matrix.matrix_multiplication(og_trans_i, new Matrix(s));
		new_scalar = Matrix.matrix_multiplication(new_scalar, og_trans);
		if(scalar_matrix == null) {
			scalar_matrix = new Matrix(new_scalar);
		} else {
			scalar_matrix = Matrix.matrix_multiplication(scalar_matrix, new_scalar);
		} *************/


		Matrix new_rotation =new Matrix(Matrix.matrix_multiplication(get_transformation_inverse(og_trans), RWt));
		new_rotation = Matrix.matrix_multiplication(new_rotation, RZ);
		new_rotation = Matrix.matrix_multiplication(new_rotation, RW);
		new_rotation = Matrix.matrix_multiplication(new_rotation, og_trans);
		if(rotation_matrix == null) {
			rotation_matrix = new Matrix(new_rotation);
		} else {
			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, new_rotation);
		}
		/*
		if(rotation_matrix == null) {
			rotation_matrix = Matrix.matrix_multiplication(get_transformation_inverse(og_trans), RWt);
			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, RZ);
			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, RW);
			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, og_trans);
		} else {

			Matrix new_rotation =new Matrix(Matrix.matrix_multiplication(get_transformation_inverse(og_trans), RWt));
			new_rotation = Matrix.matrix_multiplication(new_rotation, RZ);
			new_rotation = Matrix.matrix_multiplication(new_rotation, RW);
			new_rotation = Matrix.matrix_multiplication(new_rotation, og_trans);

			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, new_rotation);
		} */
	}
	
	public static float get_radians(float d) {
		float r =  d * (float) Math.PI;
		r = r/180;
		return r;
	}
	
	public static float get_magnitude(float[] v) {
		float sum = 0;
		for(int i = 0; i < v.length-1; i++) {
			sum += v[i] * v[i];
		}
		float mag = (float) Math.sqrt(sum);
		return mag;
		
	}
	
	public static float[] cross_product(float[] a, float[] b) {
		//System.out.println("\ta normalized: " + a[0] + ", " + a[1] + ", " + a[2] + ", " + a[3]);
		//System.out.println("\tb normalized: " + b[0] + ", " + b[1] + ", " + b[2] + ", " + b[3]);
		float[] p = new float[4];
		p[0] = (a[1]*b[2] - a[2]*b[1]);
		p[1] = (a[2]*b[0] - a[0]*b[2]);
		p[2] = (a[0]*b[1] - a[1]*b[0]);
		p[3] = 0;
		//System.out.println("\tp normalized: " + p[0] + ", " + p[1] + ", " + p[2] + ", " + p[3]);
		return p;
	}
	
	public static float[] normalize(float[] v) {
		float mag = get_magnitude(v);
		if(mag == 0) {
			return v;
		}
		float[] nv = new float[v.length];
		for(int i = 0; i < v.length-1; i++) {
			nv[i] = v[i]/mag;
		}
		nv[3] = 1;
		return nv;
	}
	
	public static void finish() {
		if(transformation_matrix == null) {
			transformation_matrix = Matrix.get_identity(4);
		}
		
		if(rotation_matrix == null) {
			rotation_matrix = Matrix.get_identity(4);
		}
		
		if(scalar_matrix == null) {
			scalar_matrix = Matrix.get_identity(4);
		} 
		
		Matrix product;/*
		product = Matrix.matrix_multiplication(get_transformation_inverse(transformation_matrix), rotation_matrix);
		product = Matrix.matrix_multiplication(product, transformation_matrix);
		product = Matrix.matrix_multiplication(product, scalar_matrix);
		product = Matrix.matrix_multiplication(product, points);*/
		//System.out.println("Applying scalar:\n"+scalar_matrix);
		//System.out.println("Applying translation:\n"+transformation_matrix);
		//System.out.println("Applying rotation:\n"+rotation_matrix);		
	
		//WORKS!!
		//product = Matrix.matrix_multiplication(scalar_matrix, transformation_matrix);
		//product = Matrix.matrix_multiplication(product, rotation_matrix);
		//points = Matrix.matrix_multiplication(product, points);
		
		//Works better!!!
		product = Matrix.matrix_multiplication(transformation_matrix, scalar_matrix);
		product = Matrix.matrix_multiplication(product, rotation_matrix);
		points = Matrix.matrix_multiplication(product, points);
	}
	
	public static Matrix get_transformation_inverse(Matrix m){
		float[][] mI = new float[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				mI[i][j] = m.matrix[i][j];
			}
		}
		mI[0][3] = -1f * mI[0][3];
		mI[1][3] = -1f * mI[1][3];
		mI[2][3] = -1f * mI[2][3];
		return new Matrix(mI);
	}

    public static void main(String[] args) throws IOException {
        do_input(args[0]);
        //System.out.println(points);
        get_user_io();
        do_output(args[1]);
        
       

    }

}
