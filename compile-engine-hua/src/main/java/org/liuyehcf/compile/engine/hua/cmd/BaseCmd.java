package org.liuyehcf.compile.engine.hua.cmd;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Cmd基类
 *
 * @author hechenfeng
 * @date 2018/6/28
 */
class BaseCmd {

    private final List<OptInfo> optInfoList = new ArrayList<>();

    void parse(String[] args) throws ParseException {
        Options options = new Options();

        optInfoList.forEach((optInfo -> {
            if (optInfo.isRequired) {
                options.addRequiredOption(optInfo.opt, optInfo.longOpt, optInfo.hasArg, optInfo.description);
            } else {
                options.addOption(optInfo.opt, optInfo.longOpt, optInfo.hasArg, optInfo.description);
            }
        }));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            throw e;
        }

        optInfoList.forEach(
                (optInfo -> {
                    if (cmd.hasOption(optInfo.opt)) {
                        String optValue = cmd.getOptionValue(optInfo.opt);
                        optInfo.optAction.execute(optValue);
                    }
                })
        );
    }

    /**
     * 注册选项信息
     *
     * @param opt         选项
     * @param longOpt     选项完整信息
     * @param isRequired  是否必须
     * @param hasArg      是否有参数
     * @param description 选项描述
     * @param optAction   选项动作
     */
    void registerOption(String opt, String longOpt, boolean isRequired, boolean hasArg, String description, OptAction optAction) {
        optInfoList.add(new OptInfo(opt, longOpt, isRequired, hasArg, description, optAction));
    }

    BaseCmd() {
        registerOption("h", "help", false, false, "help", (optValue) -> {
            System.out.println("Options:");
            optInfoList.forEach((optInfo -> System.out.format("\t%-1s,%-10s%-20s\n", optInfo.opt, optInfo.longOpt, optInfo.description)));
            System.exit(0);
        });
    }

    private static final class OptInfo {
        /**
         * 选项
         */
        private final String opt;

        /**
         * 完整选项
         */
        private final String longOpt;

        /**
         * 是否必须
         */
        private final boolean isRequired;

        /**
         * 是否有参数
         */
        private final boolean hasArg;

        /**
         * 选项描述
         */
        private final String description;

        /**
         * 选项动作
         */
        private final OptAction optAction;

        private OptInfo(String opt, String longOpt, boolean isRequired, boolean hasArg, String description, OptAction optAction) {
            this.opt = opt;
            this.longOpt = longOpt;
            this.isRequired = isRequired;
            this.hasArg = hasArg;
            this.description = description;
            this.optAction = optAction;
        }
    }
}
