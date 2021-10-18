package com.util;

import java.io.IOException;
import java.util.ArrayDeque;

import com.MainApp;
import com.enums.Language;
import com.models.Word;

import javafx.concurrent.Task;

/**
 * Handles all TTS for the Kemu Kupu Application
 */
public class TTS {
    private ArrayDeque<ProcessBuilder> festivalQueue;
    private boolean speaking;

    //// Private (Helper) Methods ////

    /**
     * Pull the next word from the queue and read it.
     */
    private void __speakNext() {
        if (speaking || festivalQueue.peek() == null)
            return;
        speaking = true;
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    Process p = festivalQueue.poll().start();
                    p.waitFor();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                speaking = false;
                __speakNext();
                return null;
            }
        };
        new Thread(task).start();
    }

    //// Public Methods ////

    public TTS() {
        this.festivalQueue = new ArrayDeque<>();
        this.speaking = false;
    }

    /**
     * Reads a word to a user utilising festival
     * 
     * @param word     The word or phrase to be read to the user
     * @param repeats  The number of times to repeat the word or phrase
     * @param language The language to which words are read out using
     */
    public void readWord(Word word, int repeats, Language language) throws Exception {
        if (word == null)
            throw new Exception("param `word` may not be null");
        if (language == null)
            throw new Exception("param `language` may not be null");
        if (repeats < 1 || repeats > 20)
            throw new Exception("repeats should be between 1 and 20 (inclusive)");
        String speedCommand = "\"(Parameter.set 'Duration_Stretch " + MainApp.getSetting().getDurationFactor() + ")\"";
        String langCommand = (language == Language.MAORI) ? "voice_akl_mi_pk06_cg" : "voice_akl_nz_cw_cg_cg";
        String wordRaw = (language == Language.MAORI) ? word.getMaori() : word.getEnglish();
        if (wordRaw == null)
            throw new Exception("Attempted to read " + ((language == Language.MAORI) ? "maori" : "english")
                    + " word, which was null!");
        String wordCommand = "\"(SayText \\\"" + wordRaw + "\\\")\"";
        for (int i = 0; i < Integer.max(repeats, 1); i++) {
            ProcessBuilder c = new ProcessBuilder("/bin/bash", "-c",
                    "echo \"(" + langCommand + ")\" " + speedCommand + " " + wordCommand + " | festival");
            festivalQueue.add(c);
        }
        __speakNext();
    }

    public void stopSpeech() {
        festivalQueue.clear();
    }
}