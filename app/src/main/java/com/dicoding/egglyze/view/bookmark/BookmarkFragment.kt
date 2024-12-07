package com.dicoding.egglyze.view.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.egglyze.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarkAdapter: BookmarkAdapter
    private lateinit var bookmarkViewModel: BookmarkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inisialisasi ViewModel
        bookmarkViewModel = ViewModelProvider(this).get(BookmarkViewModel::class.java)

        // Menginflate binding untuk fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi RecyclerView dan Adapter
        binding.rvBookmark.layoutManager = LinearLayoutManager(requireContext())

        // Mengamati data di ViewModel
        bookmarkViewModel.bookmarks.observe(viewLifecycleOwner) { bookmarks ->
            // Menyambungkan Adapter dengan data
            bookmarkAdapter = BookmarkAdapter(bookmarks)
            binding.rvBookmark.adapter = bookmarkAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
