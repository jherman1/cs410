package pa1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class pa1 {
	
	public static String header = "";
	public static String faces = "";
	public static int num_vertices = 0;
	public static int num_faces = 0;
	public static Matrix points;
	public static Matrix scalar_matrix = null;
	public static Matrix transformation_matrix = null;
	public static Matrix rotation_matrix = null;
	
	public static void do_input(String inputfile) throws IOException {
				float[][] vertices = null;

	            Scanner scan = new Scanner(new File(inputfile));
	            boolean part_of_header = true;
	            int counter = 0;
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
	                    	   if(i == 3) {
	                        		vertices[i][counter] = 1f;
	                        		continue;
	                        	}
	                        	vertices[i][counter] = Float.parseFloat(line_parts[i]);
	                        	
	                        }
	                        counter++;
	                    } else {
	                    	faces += line + "\n";
	                    }
	                    
	                }
	                
	            
	            }
	         
	            points = new Matrix(vertices);
	            System.out.println(points);
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
		out.write(faces);
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
		System.out.println("Scale");
		float[][] s = {{sx, 0f, 0f, 0f}, {0f, sy, 0f, 0f}, {0f, 0f, sz, 0f}, {0f, 0f, 0f, 1}};
		if(scalar_matrix == null) {
			scalar_matrix = new Matrix(s);
		} else {
			scalar_matrix = Matrix.matrix_multiplication(scalar_matrix, new Matrix(s));
		}
	}
	
	public static void translate(float tx, float ty, float tz) {
		System.out.println("Translate");
		float[][] t = {{1f, 0f, 0f, tx}, {0f, 1f, 0f, ty}, {0f, 0f, 1f, tz}, {0f, 0f, 0f, 1f}};
		if(transformation_matrix == null) {
			transformation_matrix = new Matrix(t);
		} else {
			transformation_matrix = Matrix.matrix_multiplication(transformation_matrix, new Matrix(t));
		}
	}

	public static void rotate(float rx, float ry, float rz, float theta) {
		System.out.println("Rotate");
		float[] vec = {rx, ry, rz, 0};
		float[] w = normalize(vec);
		//System.out.println(nv[0] + ", " + nv[1] + ", " + nv[2] + ", " + nv[3]);
		w[3] = 1;
		float min = w[0];
		int min_pos = -1;
		for(int i = 0; i < 3; i++) {
			if(w[i] < min) {
				min = w[i];
				min_pos = i;
			}
		}
		float[] m = new float[4];
		for(int i = 0; i < 3; i++) {
			if(i == min_pos) {
				m[i] = 1;
			} else {
				m[i] = w[i];
			}
		}
		m[3] = 0;
		m = normalize(m);
		m[3] = 1;
		
		float[] u = cross_product(w, m);
		float[] v = cross_product(w, u);
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
		Rz[0][0] = (float) Math.cos(theta); Rz[0][1] = (float) -Math.sin(theta); Rz[0][2] = 0; Rz[0][3] = 0;
		Rz[1][0] = (float) Math.sin(theta); Rz[1][1] = (float) Math.cos(theta); Rz[1][2] = 0; Rz[1][3] = 0;
		Rz[2][0] = 0; Rz[2][1] = 0;  Rz[2][2] = 1; Rz[2][3] = 0;
		Rz[3][0] = 0; Rz[3][1] = 0;  Rz[3][2] = 0; Rz[3][3] = 1;
		
		Matrix RZ = new Matrix(Rz);
		Matrix RWt = null;
		if(RW.is_orthonormal()) {
			RWt = Matrix.get_transpose(RW);
		} else {
			System.out.println("Not an orthonormal matrix!");
			System.exit(-1);
		}
		
		if(rotation_matrix == null) {
			rotation_matrix = Matrix.matrix_multiplication(RWt, RZ);
			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, RW);
		} else {
			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, RWt);
			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, RZ);
			rotation_matrix = Matrix.matrix_multiplication(rotation_matrix, RW);
		}
	}
	
	public static float get_magnitude(float[] v) {
		float sum = 0;
		for(int i = 0; i < v.length; i++) {
			sum += v[i] * v[i];
		}
		float mag = (float) Math.sqrt(sum);
		return mag;
		
	}
	
	public static float[] cross_product(float[] a, float[] b) {
		float[] p = new float[4];
		p[0] = (a[1]*b[2] - a[2]*b[1]);
		p[1] = (a[2]*b[0] - a[0]*b[2]);
		p[2] = (a[0]*b[1] - a[1]*b[0]);
		p[3] = 1;
		return p;
	}
	
	public static float[] normalize(float[] v) {
		float mag = get_magnitude(v);
		float[] nv = new float[v.length];
		for(int i = 0; i < v.length; i++) {
			nv[i] = v[i]/mag;
		}
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
		
		Matrix product;
		product = Matrix.matrix_multiplication(scalar_matrix, transformation_matrix);
		product = Matrix.matrix_multiplication(product, rotation_matrix);
		points = Matrix.matrix_multiplication(product, points);
	}

    public static void main(String[] args) throws IOException {
        do_input(args[0]);
        //System.out.println(points);
        get_user_io();
        do_output(args[1]);
        
       

    }

}
