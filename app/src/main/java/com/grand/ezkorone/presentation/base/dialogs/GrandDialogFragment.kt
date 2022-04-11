package com.grand.ezkorone.presentation.base.dialogs

import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CallSuper
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.grand.ezkorone.R
import com.grand.ezkorone.core.extensions.*
import com.grand.ezkorone.databinding.DialogFragmentGrandBinding
import com.grand.ezkorone.databinding.LayoutGrandBinding
import kotlin.math.roundToInt

class GrandDialogFragment : MADialogFragment<DialogFragmentGrandBinding>() {

    companion object {
        private const val TAG = "com.grand.ezkorone.presentation.base.dialogs.GrandDialogFragment"

        fun show(manager: FragmentManager): GrandDialogFragment {
            val dialogFragment = GrandDialogFragment()
            dialogFragment.show(manager, TAG)
            return dialogFragment
        }

        fun show(context: Context) {
            val binding = DataBindingUtil.inflate<LayoutGrandBinding>(
                context.layoutInflater, R.layout.layout_grand, null, false
            )

            val dialog = AlertDialog.Builder(context, R.style.CustomDialog123).create()
            dialog.setView(binding.root)

            dialog.show()

            binding.apply {
                tvGrandUrl.setOnClickListener {
                    it.context?.launchBrowser("http://${tvGrandUrl.text?.toString()}")
                }

                rlGrandCall.setOnClickListener {
                    it.context?.launchDialNumber(tvGrandPhone.text?.toString().orEmpty())
                }

                rlGrandWhats.setOnClickListener {
                    it.context?.launchWhatsApp(tvGrandPhone.text?.toString().orEmpty())
                }

                imgGrandClose.setOnClickListener {
                    dialog.dismiss()
                }
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.dialog_fragment_grand

    @CallSuper
    override fun onCreateDialogWindowChanges(window: Window) {
        val drawable = InsetDrawable(
            AppCompatResources.getDrawable(requireContext(), R.drawable.border_white_16),
            requireContext().dpToPx(16f).roundToInt()
        )
        window.setBackgroundDrawable(drawable)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.grandLayout?.apply {
            tvGrandUrl.setOnClickListener {
                context?.launchBrowser("http://${tvGrandUrl.text?.toString()}")
            }

            rlGrandCall.setOnClickListener {
                context?.launchDialNumber(tvGrandPhone.text?.toString().orEmpty())
            }

            rlGrandWhats.setOnClickListener {
                context?.launchWhatsApp(tvGrandPhone.text?.toString().orEmpty())
                /*
                layoutGrandBinding..setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent i = new Intent("android.intent.action.MAIN");
                            i.setComponent(new ComponentName("com.whatsapp","com.whatsapp.Conversation"));
                            i.putExtra("jid", PhoneNumberUtils.stripSeparators(layoutGrandBinding.tvGrandPhone.getText().toString())+"@s.whatsapp.net");
                            context.startActivity(i);
                        } catch (Exception e) {
                            Log.e("exc",e.getMessage().toString());
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp&hl=en"));
                            context.startActivity(browserIntent);
                        }
                    }
                });
                     */
            }

            imgGrandClose.setOnClickListener {
                dismiss()
            }
        }
    }

}
