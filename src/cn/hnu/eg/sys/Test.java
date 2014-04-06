package cn.hnu.eg.sys;

import java.io.File;
import java.io.IOException;

import cn.hnu.eg.util.EGConstant;

public class Test {

	public static void main(String[] args) throws IOException {
				File f = new File(EGConstant.SolutionPath + 3 + File.separator
				+ 1+".dat");
		f.createNewFile();
	}

}
