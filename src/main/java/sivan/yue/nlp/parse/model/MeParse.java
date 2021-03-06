package sivan.yue.nlp.parse.model;

import sivan.yue.nlp.common.dataAlgo.matrix.towDimMatrix.SparseDMatrix;
import sivan.yue.nlp.common.dataAlgo.probability.model.ConditionalDRate;
import sivan.yue.nlp.common.dataAlgo.viterbi.module.MeViterbi;
import sivan.yue.nlp.common.tools.CConst;
import sivan.yue.nlp.common.tools.FileIteratorUtil;
import sivan.yue.nlp.parse.BaseParse;

import java.io.IOException;

/**
 * Created by xiwen.yxw on 2017/3/28.
 */
public class MeParse extends BaseParse{

    private ConditionalDRate rateModule;

    private MeViterbi viterbi = new MeViterbi();

    public MeParse(ConditionalDRate rate) {
        this.rateModule = rate;
        viterbi.setRate(rateModule);
    }

    @Override
    public int[] parse(int[] words) {
        return viterbi.viterbi(words, stateNum);
    }

    @Override
    protected void loadModel(String path) {
        rateModule = new ConditionalDRate();
        SparseDMatrix matrix = new SparseDMatrix(stateNum, viewNum);
        rateModule.setRate(matrix);
        String fileName = path + "/"+ CConst.ME_NAME;
        try {
            for (String str : FileIteratorUtil.readLines(fileName)) {
                String[] strArr = str.split("\\|");
                if (strArr.length != 3) {
                    continue;
                }
                int x = Integer.parseInt(strArr[0]);
                int y = Integer.parseInt(strArr[1]);
                double rate = Double.parseDouble(strArr[2]);
                rateModule.setRate(x, y, rate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        viterbi.setRate(rateModule);
    }

    public ConditionalDRate getRateModule() {
        return rateModule;
    }

    public void setRateModule(ConditionalDRate rate) {
        this.rateModule = rate;
        viterbi.setRate(rate);
    }
}
