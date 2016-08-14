package com.yankovets.sqlcmd.controller.command;

import com.yankovets.sqlcmd.model.DataSet;
import com.yankovets.sqlcmd.model.DatabaseManager;
import com.yankovets.sqlcmd.view.View;

public class Find implements Command{
    private DatabaseManager manager;
    private View view;


    public Find(DatabaseManager manager, View view) {
        this.manager = manager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String command) {
        return command.startsWith("find|");
    }

    @Override
    public void process(String command) {
        String[] data = command.split("[|]");

        if (data.length != 2){
            throw new IllegalArgumentException(String.format("The amount of arguments for this command," +
                    " which split by '|' are %s, but expected 2", data.length));
        }

        String tableName = data[1];

        DataSet[] tableData = manager.getTableData(tableName);
        String[] tableColumns = manager.getTableColumns(tableName);

        printHeader(tableColumns);
        printTable(tableData);
    }

    private void printTable(DataSet[] tableData) {
        for (DataSet row: tableData){
            printRow(row);
        }
        view.write("-------------------------");
    }

    private void printRow(DataSet row) {
        Object[] values = row.getValues();
        String result = "|";
        for (Object value : values) {
            result += value + "|";
        }
        view.write(result);
    }

    private void printHeader(String[] tableColumns) {
        String result = "|";
        for (String name : tableColumns) {
            result += name + "|";
        }
        view.write("-------------------------");
        view.write(result);
        view.write("-------------------------");
    }
}
