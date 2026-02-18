package com.example.recipease

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.recipease.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_navhost_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val navbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        setSupportActionBar(findViewById(R.id.toolbar))

        val appBarConfiguration = AppBarConfiguration( setOf( R.id.RecipesFeed, R.id.ProfilePage ) )

        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(navbar, navController)

        navbar.setOnApplyWindowInsetsListener(null)
        navbar.setPadding(0,0,0,0)

        overrideCenterIcon(navbar)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val navbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

            navbar.visibility =
                if (destination.id !in appBarConfiguration.topLevelDestinations) View.GONE else View.VISIBLE

            binding.toolbar.visibility =
                if (destination.id in appBarConfiguration.topLevelDestinations) View.GONE else View.VISIBLE
        }
    }

    @SuppressLint("RestrictedApi")
    private fun overrideCenterIcon(navbar: BottomNavigationView) {
        val menuView = navbar.getChildAt(0).safeCast<BottomNavigationMenuView>() ?: return
        val targetItem = menuView.children
            .find { it.id == R.id.AddRecipe }
            ?.safeCast<BottomNavigationItemView>()
            ?: return
        targetItem.setIconTintList(null)
        val sizeInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            48f,
            resources.displayMetrics
        ).toInt()
        targetItem.setIconSize(sizeInPx)
        targetItem.setItemPaddingBottom(0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> navController.navigateUp()
            else -> NavigationUI.onNavDestinationSelected(
                item,
                navController
            )
        }
    }

    fun <T> Any.safeCast(): T? = this as? T
}