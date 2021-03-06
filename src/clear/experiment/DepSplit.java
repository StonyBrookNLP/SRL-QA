/**
* Copyright (c) 2009, Regents of the University of Colorado
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package clear.experiment;


import java.io.PrintStream;

import clear.dep.DepLib;
import clear.dep.DepTree;
import clear.reader.CoNLLXReader;
import clear.util.IOUtil;

/**
 * Predicts dependency trees.
 * 
 * <pre>
 * Usage: java harvest.DepPredic -t <test file> -o <output file> -m <model file> -c <configuration directory> [-f <flag> -a <algorithm> -l <lower bound> -u <upper bound>]
 * </pre>
 * 
 * Flags
 * - {@link DepLib#FLAG_PREDICT}: greedy search
 * - {@link DepLib#FLAG_PREDICT_BEST}: k-best search
 * 
 * Algorithms
 * - {@link DepLib#ALG_NIVRE}: Nivre's list-based, non-projective algorithm
 * - {@link DepLib#ALG_CHOI }: Choi's algorithm
 * 
 * @author Jinho D. Choi
 * <b>Last update:</b> 4/26/2010
 */
public class DepSplit
{
	public DepSplit(String filename)
	{
		CoNLLXReader reader = new CoNLLXReader(filename, true);
		DepTree   tree;
		PrintStream[] fout = new PrintStream[10];
		
		for (int i=0; i<fout.length; i++)
			fout[i] = IOUtil.createPrintFileStream(filename+"."+i);
		
		while ((tree = reader.nextTree()) != null)
		{
			int index = (tree.size() >= 101) ? 9 : (tree.size()-1) / 10;
			fout[index].println(tree+"\n");
		}
		
		for (int i=0; i<fout.length; i++)
		{
			fout[i].flush();
			fout[i].close();
		}
	}
	
	static public void main(String[] args)
	{
		new DepSplit(args[0]);
	}
}
