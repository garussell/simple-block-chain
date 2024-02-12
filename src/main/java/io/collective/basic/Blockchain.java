package io.collective.basic;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

public class Blockchain {
    private final LinkedList<Block> blocks;

    public Blockchain() {
        this.blocks = new LinkedList<>();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public void add(Block block) {
        blocks.add(block);
    }

    public int size() {
        return blocks.size();
    }

    public boolean isValid() throws NoSuchAlgorithmException {
        // Check mined
        for (Block block : blocks) {
            if (!isMined(block)) {
                return false; // Block is not mined
            }
        }

        // Check previous hash matches
        for (int i = 1; i < blocks.size(); i++) {
            if (!blocks.get(i).getPreviousHash().equals(blocks.get(i - 1).getHash())) {
                return false; // Previous hash does not match
            }
        }

        // Check hash is correctly calculated
        for (Block block : blocks) {
            if (!block.getHash().equals(block.calculatedHash())) {
                return false; // Hash is not correctly calculated
            }
        }

        return true; // All checks passed
    }

    /// Supporting functions that you'll need.

    public static Block mine(Block block) throws NoSuchAlgorithmException {
        Block mined = new Block(block.getPreviousHash(), block.getTimestamp(), block.getNonce());

        while (!isMined(mined)) {
            mined = new Block(mined.getPreviousHash(), mined.getTimestamp(), mined.getNonce() + 1);
        }
        return mined;
    }

    public static boolean isMined(Block minedBlock) {
        return minedBlock.getHash().startsWith("00");
    }
}