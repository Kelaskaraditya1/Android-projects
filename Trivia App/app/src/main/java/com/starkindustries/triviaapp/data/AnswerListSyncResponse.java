package com.starkindustries.triviaapp.data;

import com.starkindustries.triviaapp.Model.Questions;

import java.util.ArrayList;

public interface AnswerListSyncResponse {
    void processFinished(ArrayList<Questions> questionlist);
}
