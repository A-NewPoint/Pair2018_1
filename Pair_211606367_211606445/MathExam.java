
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MathExam {
	static Scanner input = new Scanner(System.in);
	static String filename ="out.txt";	
	static File file = new File(filename),parent = null;
	static OutputStream out = null;
	static String[] str_symbol = {"+","-","x","÷"},input_args= {" "," "};
	static List<String> Calculation_Problem = new ArrayList<String>(),
						Word_Set = new ArrayList<String>();
	static int cal_number1 = 0, cal_number2 = 0,symbol = 0, sum = 0, remainder = 0, 
			   number = -1, grade = -1, 
			   calculation_num = 0 ,ran_symbol_num = 0,ran_left_parenthesis_num = 0;			
	static Random ranNum = new Random(),ranSymbol = new Random(),ranSymbolNum = new Random(),ran_left_parenthesis = new Random();
	static String word = null,check_input_message = null;
	static Pattern pattern = Pattern.compile("[0-9]*");
	public static void main(String[] args) {
			File_Initialization(file);//文档初始化生成
			Input_Message(args);//输入信息检测
			for (int i = 1; i <= number; i++) 
			{
				Iteration(i);//生成一道合格的题目
				File_Write_Problem(i);//将这道题目写入文档
			}
			if(number != 0)
			{
				File_Write_Answer();//将所有题目的答案写入文档
				System.out.println("题目已经生成，请看out.txt文档");
			}	
	}
	
	/**
		小学三年级四则混合运算的要求如下：
		运算符在2～4个
		可以加括号
		减法运算的结果不能有负数--还需要在逆波兰计算时仍有判断
		除法运算除数不能为0，不能有余数
		数字在0-1000以内，含端点
		当然，为一年级、二年级出题的功能还是要保留
		经过查询，三年级混合运算结果还不能是小数,并且括号内的数字只有两个
	 * 
	 * **/
	private static void Iteration(int i) {
		if(grade == 1)
		{
			symbol = ranSymbol.nextInt(2);
			cal_number1 = ranNum.nextInt(101);
			cal_number2 = ranNum.nextInt(101);
			if(str_symbol[symbol].equals("+") && cal_number1 + cal_number2 <= 100)
				sum = cal_number1 + cal_number2;
			else if(str_symbol[symbol].equals("-") && cal_number1 - cal_number2 >= 0)
				sum = cal_number1 - cal_number2;
			else
				Iteration(i);
			word = Integer.toString(cal_number1)+" "+str_symbol[symbol]+" "+Integer.toString(cal_number2);
			if(Word_Set.contains(word))
			{
				Iteration(i);
			}
			else
			{
				Word_Set.add(word);
				word = "("+Integer.toString(i)+") "+word;
				return;
			}
		}	
		else if(grade == 2)
		{	
			symbol = ranSymbol.nextInt(4);
			cal_number1 = ranNum.nextInt(101);
			cal_number2 = ranNum.nextInt(101);
			if(str_symbol[symbol].equals("+") && cal_number1 + cal_number2 <= 100)
				sum = cal_number1 + cal_number2;
			else if(str_symbol[symbol].equals("-")&& cal_number1 - cal_number2 >= 0)
				sum = cal_number1 - cal_number2;
			else if(str_symbol[symbol].equals("x") && cal_number1 * cal_number2 <= 100)
				sum = cal_number1 * cal_number2;
			else if(str_symbol[symbol].equals("÷") && cal_number2 != 0)
			{	
				sum = cal_number1 / cal_number2;
				remainder = cal_number1 % cal_number2;
			}
			else
				Iteration(i);
			word = Integer.toString(cal_number1)+" "+str_symbol[symbol]+" "+Integer.toString(cal_number2);
			if(Word_Set.contains(word))
			{
				Iteration(i);
			}
			else
			{
				Word_Set.add(word);
				word = "("+Integer.toString(i)+") "+word;
				return;
			}
		}
		else if(grade == 3)
		{		
			cal_number2 = ranNum.nextInt(1001);
			ran_symbol_num = ranSymbolNum.nextInt(3)+2;
			word = Integer.toString(cal_number2);
			for(int j=1;j<=ran_symbol_num;j++)
			{
				cal_number1 = cal_number2;
				cal_number2 = ranNum.nextInt(1001);
				symbol = ranSymbol.nextInt(4);
				ran_left_parenthesis_num = ran_left_parenthesis.nextInt(2);
				if(str_symbol[symbol].equals("+") && cal_number1 + cal_number2 > 1000
				|| str_symbol[symbol].equals("-") && cal_number1 - cal_number2 < 0
				|| str_symbol[symbol].equals("x") && cal_number1 * cal_number2 > 1000
				|| str_symbol[symbol].equals("÷") && j!=1 && cal_number2 == 0)
				{	
					j--;
					continue;
				}
				else if(ran_left_parenthesis_num % 2 == 1 && j <= ran_symbol_num-1)
				{	
					if(j == 1)
					{
						word = "(" + word;
						symbol = ranSymbol.nextInt(2);
						cal_number2 = ranNum.nextInt(1001);
						word = word + str_symbol[symbol] + Integer.toString(cal_number2)+")";
						symbol = ranSymbol.nextInt(2) + 2;
						cal_number2 = ranNum.nextInt(1001);
						word = word+str_symbol[symbol]+Integer.toString(cal_number2);
					}
					else if(j <= ran_symbol_num-1)
					{
						word = word + str_symbol[symbol] + "("+Integer.toString(cal_number2);
						if(symbol == 0 || symbol == 1)
							symbol = ranSymbol.nextInt(2);
						else
							symbol = ranSymbol.nextInt(4);
						cal_number2 = ranNum.nextInt(1001);
						word = word + str_symbol[symbol] + Integer.toString(cal_number2)+")";
					}
					j++;
					continue;
				}
				word = word+str_symbol[symbol]+Integer.toString(cal_number2);
			}
			Calculation calculation = new Calculation(word);
			calculation.Infix_Expression_To_Word();
			calculation.To_Suffix_Expression();
			if(calculation.Suffix_Expression_Summation())
			{				
				word = calculation.getword();
				sum = calculation.getSum();
				if(Word_Set.contains(word))
				{
					Iteration(i);
				}
				else
				{
					word = "("+i+") " + word;
					Word_Set.add(word);
					return;
				}
			}
			else
				Iteration(i);
		}
	}

	private static void Input_Message(String[] args) {
		int j=1;
		if(args[0].equals("-n") && args[2].equals("-grade"))
		{	input_args[0] = args[1];input_args[1] = args[3];}
		else if(args[0].equals("-grade") && args[2].equals("-n"))
		{	input_args[0] = args[3];input_args[1] = args[1];}
		else
			System.out.print("输入有误！！！");	
		check_input_message = input_args[0];//题数
		if(!pattern.matcher(check_input_message).matches())
		{	
			System.out.print("输入的题数不合法！请重新输入题数：");
		}
		else
		{
			number = Integer.parseInt(check_input_message);
			if(number > 1000)
				System.out.print("输入的题数太大！请重新输入题数：");
		}
		j++;
		while(number<0 || number > 1000)
		{
			if(j!=1)
				check_input_message = input.nextLine();
			if(!pattern.matcher(check_input_message).matches())
			{	
				System.out.print("输入的题数不合法！请重新输入题数：");
			}
			else
			{
				number = Integer.parseInt(check_input_message);
				if(number > 1000)
					System.out.print("输入的题数太大！请重新输入题数：");
			}	
		}
		j=1;
		check_input_message = input_args[1];//年级
		if(!pattern.matcher(check_input_message).matches())
		{	
			System.out.print("输入的年级不合法！请重新输入年级：");
		}	
		else
		{
			grade = Integer.parseInt(check_input_message);
			if(grade<1 || grade>3)
				System.out.print("输入的年级不合法!请重新输入年级(1或2或3)：");			
		}
		j++;
		while(grade<1 || grade>3)
		{
			if(j!=1)
				check_input_message = input.nextLine();
			if(!pattern.matcher(check_input_message).matches())
			{
				System.out.print("输入的年级不合法!请重新输入年级(1或2或3)：");
			}
			else
			{
				grade = Integer.parseInt(check_input_message);
				if(grade<1 || grade>3)
					System.out.print("输入的年级不合法!请重新输入年级(1或2或3)：");
			}
			
		}
	}
	
	private static void File_Write_Answer() {
		try {
			out.write("\r\n".getBytes());
			for (int i = 0; i < calculation_num; i++)
			{
				out.write(Calculation_Problem.get(i).getBytes());
			}
		} 
		catch (IOException e) {
			System.out.println("程序在写入计算题答案时异常："+e.getMessage());
		}
	}

	private static void File_Write_Problem(int i) {
		try {
			
			out.write((word+"\r\n").getBytes());
			if(remainder!=0)
			{
				Calculation_Problem.add(word+" = "+Integer.toString(sum)+"..."+Integer.toString(remainder)+"\r\n");
				calculation_num++;
				remainder = 0;
			}
			else
			{	
				Calculation_Problem.add(word+" = "+Integer.toString(sum)+"\r\n");
				calculation_num++;
			}
			word = "";
		} 
		catch (IOException e) 
		{
			System.out.println("程序在out.write()时抛出异常"+e.getMessage());
		}
	}

	private static void File_Initialization(File file) {
		if(!file.exists()) 
		{		parent = file.getParentFile();
				if(parent !=null && !parent.exists())
				{	parent.mkdir();//创建目录
					System.out.println("创建目录："+parent);
				}
				try {
					file.createNewFile();
				} catch (IOException e) {
					System.out.println("程序在file.createNewFile()时抛出异常"+e.getMessage());
				}
				System.out.println("创建新文件："+file.getAbsolutePath());
		}//获取绝对路径
		try {
			out = new FileOutputStream(file);
		} 
		catch (FileNotFoundException e) {
			System.out.println("程序在new FileOutputStream(file)时抛出异常"+e.getMessage());
		}
	}

}
