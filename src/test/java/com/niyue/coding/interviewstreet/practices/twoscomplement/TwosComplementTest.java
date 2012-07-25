package com.niyue.coding.interviewstreet.practices.twoscomplement;

import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.niyue.coding.interviewstreet.BaseTest;
import com.niyue.coding.interviewstreet.DataLoader;

@RunWith(Parameterized.class)
public class TwosComplementTest extends BaseTest {
    public TwosComplementTest(String input, String output) {
        super(input, output);
    }
    
    @Parameters
    public static Collection<String[]> data() {
        return DataLoader.load(TwosComplementTest.class);
    }

    @Override
    protected void solve() throws Exception {
        Solution solution = new Solution();
        solution.solve();
    }
}
