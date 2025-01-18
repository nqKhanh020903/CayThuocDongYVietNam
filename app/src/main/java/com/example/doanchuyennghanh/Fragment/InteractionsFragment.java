package com.example.doanchuyennghanh.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanchuyennghanh.Adapter.CommentAdapter;
import com.example.doanchuyennghanh.Domain.Comment;
import com.example.doanchuyennghanh.Domain.Herb;
import com.example.doanchuyennghanh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class InteractionsFragment extends Fragment {

    private static final String ARG_MEDICINE = "medicine";
    private RecyclerView rvComments;
    private EditText etCommentInput;
    private ImageButton btnSendComment;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private Herb selectedHerb;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedHerb = (Herb) getArguments().getSerializable("medicine");
        }
        View view = inflater.inflate(R.layout.fragment_interactions, container, false);

        // Ánh xạ view
        rvComments = view.findViewById(R.id.rvComments);
        etCommentInput = view.findViewById(R.id.etCommentInput);
        btnSendComment = view.findViewById(R.id.btnSendComment);

        // Khởi tạo danh sách comment
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(commentList);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComments.setAdapter(commentAdapter);

        // Load bình luận từ Firebase
        loadComments();

        // Gửi bình luận
        btnSendComment.setOnClickListener(v -> {
            String commentText = etCommentInput.getText().toString().trim();
            if (!commentText.isEmpty()) {
                sendComment(commentText);
                etCommentInput.setText("");
            }
        });

        return view;
    }

    private void loadComments() {

        if (selectedHerb == null) {
            Toast.makeText(getContext(), "Không tìm thấy thông tin cây thuốc!", Toast.LENGTH_SHORT).show();
            return;
        }

        int cayThuocId = selectedHerb.getId(); // Lấy ID của cây thuốc từ Herb object

        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("Comments");

        commentsRef.orderByChild("CayThuocId").equalTo(cayThuocId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        commentList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Comment comment = dataSnapshot.getValue(Comment.class);
                            commentList.add(comment);
                        }
                        commentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Lỗi khi tải bình luận!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void sendComment(String commentText) {
        if (selectedHerb == null) {
            Toast.makeText(getContext(), "Không tìm thấy thông tin cây thuốc!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) {
            Toast.makeText(getContext(), "Bạn cần đăng nhập để bình luận!", Toast.LENGTH_SHORT).show();
            return;
        }

        int cayThuocId = selectedHerb.getId(); // Lấy ID của cây thuốc từ Herb object

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.child("username").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                String userName = task.getResult().getValue(String.class);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                // Tạo node bình luận
                DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("Comments");
                String commentId = commentsRef.push().getKey(); // Tạo ID ngẫu nhiên cho bình luận

                // Tạo đối tượng bình luận
                Map<String, Object> comment = new HashMap<>();
                comment.put("CayThuocId", cayThuocId);
                comment.put("UserId", userId);
                comment.put("UserName", userName);
                comment.put("CommentText", commentText);
                comment.put("Date", date);

                // Gửi bình luận lên Firebase
                commentsRef.child(commentId).setValue(comment)
                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Bình luận đã được gửi!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Gửi bình luận thất bại!", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getContext(), "Không lấy được tên người dùng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
